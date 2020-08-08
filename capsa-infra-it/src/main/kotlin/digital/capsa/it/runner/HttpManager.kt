package digital.capsa.it.runner

import com.fasterxml.jackson.databind.ObjectMapper
import digital.capsa.it.aggregate.AggregateBuilderContext
import digital.capsa.it.json.JsonPathModifyer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.URI
import java.util.stream.Collectors

@Component
class HttpManager {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Suppress("TooGenericExceptionThrown")
    fun sendHttpRequest(
            requestJsonFileName: String,
            memento: HashMap<String, String>? = null,
            transformationData: Map<String, Any?>
    ): ResponseEntity<String> {
        val requestJson = javaClass.getResourceAsStream(requestJsonFileName)
                .let(::InputStreamReader)
                .let(::BufferedReader)
                .lines()
                .collect(Collectors.joining())

        val transformRequestJson: String = JsonPathModifyer.modifyJson(requestJson, transformationData, null)
        val httpRequest = objectMapper.readValue(transformRequestJson, HttpRequest::class.java)

        val restTemplate = RestTemplate(
                getClientHttpRequestFactory(httpRequest.connectTimeout, httpRequest.readTimeout))

        val headers = HttpHeaders()

        memento?.let {
            it["jwtToken"]?.let { token ->
                headers.setBearerAuth(token)
            }
        }

        httpRequest.headers.forEach { (key, value) -> headers[key] = value }

        val requestEntity = HttpEntity(httpRequest.body.toString(), headers)

        return restTemplate.exchange(
                URI(httpRequest.schema, null, httpRequest.host, httpRequest.port,
                        (httpRequest.basePath?.let { "${httpRequest.basePath}" } ?: "")
                                + httpRequest.path, httpRequest.queryParams, null).toString(),
                httpRequest.method, requestEntity, String::class.java)
    }

    fun uploadFile(
            endpoint: String,
            context: AggregateBuilderContext,
            name: String,
            testFilePath: String
    ): ResponseEntity<String> {

        val file = File(HttpManager::class.java.getResource(testFilePath).file)

        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA
        context.memento["jwtToken"]?.let { token ->
            headers.setBearerAuth(token)
        }
        val body: MultiValueMap<String, Any> = LinkedMultiValueMap()
        body.add(name, FileSystemResource(file))

        val requestEntity: HttpEntity<MultiValueMap<String, Any>> = HttpEntity(body, headers)

        return RestTemplate()
                .postForEntity("$endpoint/api/uploadFile", requestEntity, String::class.java)
    }

    private fun getClientHttpRequestFactory(connectTimeout: Int, readTimeout: Int):
            HttpComponentsClientHttpRequestFactory {
        val clientHttpRequestFactory = HttpComponentsClientHttpRequestFactory()
        clientHttpRequestFactory.setConnectTimeout(connectTimeout)
        clientHttpRequestFactory.setReadTimeout(readTimeout)
        return clientHttpRequestFactory
    }


}
