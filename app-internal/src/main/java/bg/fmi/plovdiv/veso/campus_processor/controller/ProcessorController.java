package bg.fmi.plovdiv.veso.campus_processor.controller;

import bg.fmi.plovdiv.veso.campus_processor.dto.ProcessRequestDTO;
import bg.fmi.plovdiv.veso.campus_processor.dto.ProcessResponseDTO;
import bg.fmi.plovdiv.veso.campus_processor.service.ProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for internal processor service endpoints.
 * Handles data processing requests from app-public service.
 * This service is only accessible within the internal Docker network.
 */
@RestController
@RequestMapping("/api")
public class ProcessorController
{
    private final ProcessorService processorService;

    @Autowired
    public ProcessorController(ProcessorService processorService)
    {
        this.processorService = processorService;
    }

    /**
     * Health check endpoint for service monitoring.
     * GET /api/health
     *
     * @return "OK" if service is healthy
     */
    @GetMapping("/health")
    public ResponseEntity<String> health()
    {

        return ResponseEntity.ok("OK");
    }

    /**
     * Process partial data (normalization and validation).
     * Called by app-public service for data processing.
     * POST /api/partial
     *
     * @param request the processing request containing input data and metadata
     * @return processed response with normalized and validated data
     */
    @PostMapping("/partial")
    public ResponseEntity<ProcessResponseDTO> processPartial(@RequestBody ProcessRequestDTO request)
    {
        ProcessResponseDTO response = processorService.processData(request);

        return ResponseEntity.ok(response);
    }
}
