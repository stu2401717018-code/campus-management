package bg.fmi.plovdiv.veso.campus_processor.service;

import bg.fmi.plovdiv.veso.campus_processor.dto.ProcessRequestDTO;
import bg.fmi.plovdiv.veso.campus_processor.dto.ProcessResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Service for processing and validating data.
 * Performs normalization, validation, and aggregation of input data.
 * Logs all operations to the persistent volume.
 */
@Service
public class ProcessorService
{
    private static final Logger logger = LoggerFactory.getLogger(ProcessorService.class);

    /**
     * Processes input data by normalizing, validating, and aggregating it.
     * Logs the processing operations for monitoring and debugging.
     *
     * @param request the processing request containing input data and metadata
     * @return processed response with normalized data, status, and processing count
     */
    public ProcessResponseDTO processData(ProcessRequestDTO request)
    {
        logger.info(
                "Processing request: input={}, metadata={}",
                request.getInput(),
                request.getMetadata()
        );

        String normalizedInput = normalizeInput(request.getInput());
        String validatedData = validateData(normalizedInput);
        Integer processedCount = aggregateData(validatedData);

        ProcessResponseDTO response = new ProcessResponseDTO();
        response.setProcessedData(validatedData);
        response.setStatus("SUCCESS");
        response.setTimestamp(LocalDateTime.now());
        response.setProcessedCount(processedCount);

        logger.info("Processing completed: status={}, processedCount={}", response.getStatus(), response.getProcessedCount());

        return response;
    }

    /**
     * Normalizes input data by trimming whitespace and converting to lowercase.
     *
     * @param input the input string to normalize
     * @return normalized string (trimmed and lowercase)
     */
    private String normalizeInput(String input)
    {
        if (input == null || input.isEmpty()) {

            return "";
        }

        return input.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * Validates data by removing invalid characters.
     * Keeps only lowercase letters, numbers, and spaces.
     *
     * @param data the data string to validate
     * @return validated string with invalid characters removed
     */
    private String validateData(String data)
    {
        if (data == null || data.isEmpty()) {
            logger.warn("Empty data received for validation");

            return "";
        }

        return data.replaceAll("[^a-z0-9\\s]", "");
    }

    /**
     * Aggregates data by counting the number of words.
     *
     * @param data the data string to aggregate
     * @return count of words in the data
     */
    private Integer aggregateData(String data)
    {
        if (data == null || data.isEmpty()) {

            return 0;
        }

        return data.split("\\s+").length;
    }
}
