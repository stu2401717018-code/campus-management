package bg.fmi.plovdiv.veso.campus_processor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Data Transfer Object for internal processing requests.
 * Contains input data and optional metadata for processing.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessRequestDTO
{
    /**
     * Input data to be processed (normalized and validated).
     */
    private String input;
    
    /**
     * Optional metadata associated with the request.
     */
    private Map<String, Object> metadata;
}
