package digital.capsa.it.tests

import digital.capsa.core.logger
import io.kubernetes.client.openapi.ApiClient
import io.kubernetes.client.openapi.Configuration
import io.kubernetes.client.openapi.apis.CoreV1Api
import io.kubernetes.client.openapi.models.V1LoadBalancerIngress
import io.kubernetes.client.util.Config
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.test.context.support.TestPropertySourceUtils
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import java.util.concurrent.TimeUnit


class ContextInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val profilesFromConsole = System.getProperty("spring.profiles.active", "")
        if (!profilesFromConsole.contains("local")) {

            val client: ApiClient = Config.defaultClient()
            Configuration.setDefaultApiClient(client)

            val commandPodIp = waitForExternalIp("metadata.name=command-app-service")?: throw Error("Command IP is null")
            println("Command Pod IP: $commandPodIp")
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                    "capsa.command.host=$commandPodIp")

            val queryPodIp = waitForExternalIp("metadata.name=query-app-service")?: throw Error("Query IP is null")
            println("Query Pod IP: $queryPodIp")
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                    "capsa.query.host=$queryPodIp")

            waitForServiceReady(commandPodIp)

            waitForServiceReady(queryPodIp)
        }
    }

    private fun getExternalIp(fieldSelector: String): String? {
        val api = CoreV1Api()
        var externalPodIp: String?
        val serviceList = api.listServiceForAllNamespaces(false, null, fieldSelector, null, 5, null, null, 100, null)
        for (service in serviceList.items) {
            logger.info("SERVICE:  " + service.metadata!!.name)
            val serviceStatus = service.status
            val balancerStatus = serviceStatus!!.loadBalancer
            logger.info("LoadBalancer: " + balancerStatus.toString())
            val ingressList = balancerStatus!!.ingress
            if (ingressList != null) {
                val iter: ListIterator<V1LoadBalancerIngress> = ingressList.listIterator()
                if (iter.hasNext()) {
                    val ingress = iter.next()
                    externalPodIp = ingress.ip
                    if (externalPodIp != null) {
                        return externalPodIp
                    }
                }
            }
        }
        return null
    }

    private fun waitForServiceReady(ip: String) {
        var live = false
        var retryCount = 0
        do {
            try {
                val response = RestTemplate().getForEntity("http://$ip/api/actuator/info", String::class.java)
                live = response.statusCode == HttpStatus.OK
            } catch (e: ResourceAccessException) {
                println("Service on $ip is not ready yet")
            }
            retryCount++
            if (!live) {
                TimeUnit.SECONDS.sleep(5)
            }
        } while (!live && retryCount < 25)
    }

    private fun waitForExternalIp(fieldSelector: String): String? {
        var ipAddr: String?
        var retryCount = 0
        do {
            ipAddr = getExternalIp(fieldSelector)
            retryCount++
            if (ipAddr == null) {
                TimeUnit.SECONDS.sleep(5)
            }
        } while (ipAddr == null && retryCount < 10)
        return ipAddr
    }
}