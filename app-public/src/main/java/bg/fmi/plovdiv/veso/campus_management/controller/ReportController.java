package bg.fmi.plovdiv.veso.campus_management.controller;

import bg.fmi.plovdiv.veso.campus_management.dto.ReportRequestDTO;
import bg.fmi.plovdiv.veso.campus_management.dto.ReportResponseDTO;
import bg.fmi.plovdiv.veso.campus_management.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Report endpoints.
 * Handles requests that require processing through the internal service.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController
{
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService)
    {
        this.reportService = reportService;
    }

    /**
     * Process report data through internal service.
     * This endpoint calls app-internal service for data processing.
     * POST /api/reports/process
     *
     * @param request the report request containing input data and metadata
     * @return processed report response with results
     */
    @PostMapping("/process")
    public ResponseEntity<ReportResponseDTO> processReport(@RequestBody ReportRequestDTO request)
    {
        ReportResponseDTO response = reportService.processReport(request);

        return ResponseEntity.ok(response);
    }
}
