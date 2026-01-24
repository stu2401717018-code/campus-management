package bg.fmi.plovdiv.veso.campus_processor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for internal processing responses.
 * Contains the processed data and processing status information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessResponseDTO
{
    /**
     * Processed data after normalization and validation.
     */
    private String processedData;
    
    /**
     * Status of the processing (e.g., "SUCCESS").
     */
    private String status;
    
    /**
     * Timestamp when the processing was completed.
     */
    private LocalDateTime timestamp;
    
    /**
     * Number of items processed (e.g., word count).
     */
    private Integer processedCount;
}
