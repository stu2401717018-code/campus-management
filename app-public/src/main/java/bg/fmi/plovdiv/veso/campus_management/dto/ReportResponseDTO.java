package bg.fmi.plovdiv.veso.campus_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for report processing responses.
 * Contains the processed result and status information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDTO
{
    /**
     * Processed result data from the internal service.
     */
    private String result;
    
    /**
     * Status of the processing (e.g., "SUCCESS", "ERROR").
     */
    private String status;
    
    /**
     * Timestamp when the processing was completed.
     */
    private LocalDateTime timestamp;
    
    /**
     * Number of items processed.
     */
    private Integer processedCount;
}
