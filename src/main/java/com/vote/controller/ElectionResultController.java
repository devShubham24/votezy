package com.vote.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vote.dto.ElectionResultRequestDTO;
import com.vote.dto.ElectionResultResponseDTO;
import com.vote.entity.ElectionResult;
import com.vote.service.ElectionResultService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/election-Results")
@CrossOrigin(origins = "*")
public class ElectionResultController {
	private ElectionResultService  electionResultService;
	private static final Logger log = LoggerFactory.getLogger(ElectionResultController.class);
    
	public ElectionResultController(ElectionResultService electionResultService) {
		super();
		this.electionResultService = electionResultService;
	}
	@PostMapping("/declare")
	public   ResponseEntity<ElectionResultResponseDTO> DeclairElectionResult(  @RequestBody @Valid  ElectionResultRequestDTO electionName) {	
		ElectionResult result  =  electionResultService.declareElectionResult(electionName.getElectionName());
		
		ElectionResultResponseDTO responseDTO =new ElectionResultResponseDTO();
		responseDTO.setElectionName(result.getElectionName());
		responseDTO.setTotalVotes(result.getTotalVotes());
		responseDTO.setWinnerId(result.getWinnerId());
		responseDTO.setWinnerVotes(result.getWinner().getVoteCount());
		
		                                         
		return ResponseEntity.ok(responseDTO);
		
	}
          
	@GetMapping("/get")
	public ResponseEntity<List<ElectionResult>> getAllResults(){
		List <ElectionResult>results=electionResultService.getAllResults();
		return ResponseEntity.ok(results);
	}

}
