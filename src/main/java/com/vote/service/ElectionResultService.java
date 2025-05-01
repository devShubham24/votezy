package com.vote.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vote.entity.Candidate;
import com.vote.entity.ElectionResult;
import com.vote.exception.ResourceNotFoundException;
import com.vote.repository.CandidateRepo;
import com.vote.repository.ElectionResultRepo;
import com.vote.repository.VoteRepo;
@Service
public class ElectionResultService {
       private CandidateRepo   candidateRepository;
       private  ElectionResultRepo     electionResultRepo;
       private   VoteRepo voteRepository;
       
	public ElectionResultService(CandidateRepo candidateRepository, ElectionResultRepo electionResultRepo,
			VoteRepo voteRepository) {
		
		this.candidateRepository = candidateRepository;
		this.electionResultRepo = electionResultRepo;
		this.voteRepository = voteRepository;}
	
		public ElectionResult declareElectionResult(String electionName) {
		Optional<ElectionResult> ResultExist=this.electionResultRepo.findByElectionName(electionName);
		if(ResultExist.isPresent()) {
			return ResultExist.get();
		}
		if(electionResultRepo.count()==1) {
			throw new ResourceNotFoundException("election result already declared with name : "+electionResultRepo.getById((long) 1).getElectionName());
		}
		
		if(voteRepository.count()==0) {
			throw new IllegalStateException("Cannot declare the result as no votes have been");
		}
		List<Candidate> allCandidate= candidateRepository.findAllByOrderByVoteCountDesc();
		if (allCandidate.isEmpty()) {
	        throw new ResourceNotFoundException("No candidates available.");
	    }

	    Candidate winner = allCandidate.get(0);

	    // Calculate total votes
	    int totalVotes = 0;

	    // Loop through all candidates to sum their vote counts
	    for (Candidate candidate : allCandidate) {
	        totalVotes += candidate.getVoteCount();
	    //    System.out.println("Candidate: " + candidate.getName() + ", Votes: " + candidate.getVoteCount());
	    }

	  //  System.out.println("Total Votes: " + totalVotes); // 
		     ElectionResult result=new ElectionResult();
		     result.setElectionName(electionName);
		     result.setWinner(winner);
		     result.setTotalVotes(totalVotes);
			return electionResultRepo.save(result);
			
		}

	    public List<ElectionResult> getAllResults(){
	    	return electionResultRepo.findAll();                
	    }
	}

