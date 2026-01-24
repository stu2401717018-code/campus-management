package bg.fmi.plovdiv.veso.campus_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Data Transfer Object for report processing requests.
 * Contains input data and optional metadata for processing.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDTO
{
    /**
     * Input data to be processed by the internal service.
     */
    private String input;
    
    /**
     * Optional metadata associated with the request (e.g., source, type).
     */
    private Map<String, Object> metadata;
}
