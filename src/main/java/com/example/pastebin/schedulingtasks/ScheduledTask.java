package com.example.pastebin.schedulingtasks;

import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.pastebin.entities.MetaData;
import com.example.pastebin.repositories.MetaDataRepo;
import com.example.pastebin.services.PostService;

import lombok.AllArgsConstructor;



@Component
@AllArgsConstructor
public class ScheduledTask {
	
	private MetaDataRepo metaDataRepo;
	private PostService postService;
	
	@Scheduled(fixedDelayString = "${interval}")
	public void deleteOutdatedPosts() {
		List<MetaData> outdatedPostsMeta = 
				metaDataRepo.findAllByExpirationDateIsLessThan(new Date());
		for (MetaData meta : outdatedPostsMeta) {
			postService.deletePost(meta.getId());
		}
		System.out.println("was deleted: " + outdatedPostsMeta.size() + " posts");
	}

}
