package digital.capsa.it.tests

import digital.capsa.core.logger
import io.kubernetes.client.openapi.ApiClient
import io.kubernetes.client.openapi.Configuration
import io.kubernetes.client.openapi.apis.CoreV1Api
import io.kubernetes.client.openapi.models.V1LoadBalancerIngress
import io.kubernetes.client.util.Config
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.support.TestPropertySourceUtils
import java.util.concurrent.TimeUnit


class ContextInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val profilesFromConsole = System.getProperty("spring.profiles.active", "")
        if (!profilesFromConsole.contains("local")) {

            val client: ApiClient = Config.defaultClient()
            Configuration.setDefaultApiClient(client)

            val externalPodIp = waitForExternalIp()
            println("LoadBalancer Ingress IP: $externalPodIp")
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                    "metrofox.host=" + externalPodIp);
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                    "metrofox.port=80");
        }
    }

    private fun getExternalIp(): String? {
        val api = CoreV1Api()
        var externalPodIp: String?
        val fieldSelector = "metadata.name=monolith-app-service"
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

    private fun waitForExternalIp(): String? {
        var ipAddr: String?
        var retryCount = 0
        do {
            ipAddr = getExternalIp()
            retryCount++
            if(ipAddr == null) {
                TimeUnit.SECONDS.sleep(5)
            }
        } while (ipAddr == null && retryCount < 10)
        return ipAddr
    }
}