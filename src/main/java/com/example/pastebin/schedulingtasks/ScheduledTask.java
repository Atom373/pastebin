package com.example.pastebin.schedulingtasks;

import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.pastebin.entities.MetaData;
import com.example.pastebin.repositories.MetaDataRepo;
import com.example.pastebin.repositories.TextBlockRepo;



@Component
public class ScheduledTask {
	
	private MetaDataRepo metaDataRepo;
	private TextBlockRepo textBlockRepo;
	
	public ScheduledTask(MetaDataRepo metaDataRepo,
						 TextBlockRepo textBlockRepo) {
		this.metaDataRepo = metaDataRepo;
		this.textBlockRepo = textBlockRepo;
	}
	
	@Scheduled(fixedDelayString = "${interval}")
	public void deleteOutdatedPosts() {
		List<MetaData> outdatedPostsMeta = 
				metaDataRepo.findAllByExpirationDateIsLessThan(new Date());
		for (MetaData meta : outdatedPostsMeta) {
			textBlockRepo.deleteTextBlock(meta.getId());
		}
		System.out.println("was deleted: " + outdatedPostsMeta.size() + " posts");
	}

}
