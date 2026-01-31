package bg.fmi.plovdiv.veso.campus_management.service;

import bg.fmi.plovdiv.veso.campus_management.dto.ReportRequestDTO;
import bg.fmi.plovdiv.veso.campus_management.dto.ReportResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for processing reports through the internal service.
 * Communicates with app-internal service via REST API.
 */
@Service
public class ReportService
{
    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    private final RestTemplate restTemplate;

    /**
     * URL of the internal service for data processing.
     * Defaults to http://app-internal:8081 if not configured.
     */
    @Value("${internal.service.url:http://app-internal:8081}")
    private String internalServiceUrl;

    @Autowired
    public ReportService(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    /**
     * Processes report data by calling the internal service.
     * Sends request to app-internal for data normalization and validation.
     *
     * @param request the report request containing input data and metadata
     * @return processed report response with results from internal service
     */
    public ReportResponseDTO processReport(ReportRequestDTO request)
    {
        logger.info("Processing report request: input={}", request.getInput());

        try {
            String url = internalServiceUrl + "/api/partial";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("input", request.getInput());
            requestBody.put("metadata", request.getMetadata());

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            Map<String, Object> responseBody = response.getBody();

            ReportResponseDTO reportResponse = new ReportResponseDTO();
            reportResponse.setResult((String) responseBody.get("processedData"));
            reportResponse.setStatus((String) responseBody.get("status"));
            reportResponse.setProcessedCount((Integer) responseBody.get("processedCount"));

            if (responseBody.get("timestamp") != null) {
                try {
                    String timestampStr = responseBody.get("timestamp").toString();
                    reportResponse.setTimestamp(java.time.LocalDateTime.parse(timestampStr));
                } catch (Exception e) {
                    reportResponse.setTimestamp(java.time.LocalDateTime.now());
                }
            } else {
                reportResponse.setTimestamp(java.time.LocalDateTime.now());
            }

            logger.info("Report processing completed: status={}", reportResponse.getStatus());

            return reportResponse;
        } catch (Exception e) {
            logger.error("Error processing report", e);
            ReportResponseDTO errorResponse = new ReportResponseDTO();
            errorResponse.setResult("Error: " + e.getMessage());
            errorResponse.setStatus("ERROR");
            errorResponse.setTimestamp(java.time.LocalDateTime.now());
            errorResponse.setProcessedCount(0);

            return errorResponse;
        }
    }
}
