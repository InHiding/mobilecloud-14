package org.magnum.mobilecloud.video.controller;

import java.security.Principal;
import java.util.Collection;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class VideoController {

	@Autowired
	VideoRepository repository;
	
	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.GET)
    public Collection<Video> getVideos(){
        return (Collection<Video>) repository.findAll();
    }
	
	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.POST)
    public Video addVideo(@RequestBody Video v){
        return repository.save(v);
    }
	
	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH+ "/{id}", method = RequestMethod.GET)
    public Video getVideo(@PathVariable("id") long id){
        return repository.findOne(id);
    }
	
	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/like", method = RequestMethod.POST)
	public ResponseEntity<Void> likeVideo(@PathVariable("id") long id,
			Principal principal) {
		Video v = repository.findOne(id);

		if (v == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}

		if (v.getLikedUsers().contains(principal.getName())) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}

		v.addLikedUser(principal.getName());
		v.setLikes(v.getLikes() + 1);
		repository.save(v);

		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/unlike", method = RequestMethod.POST)
	public ResponseEntity<Void> unlikeVideo(@PathVariable("id") long id,
			Principal principal) {
		Video v = repository.findOne(id);

		if (v == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}

		if (!v.getLikedUsers().contains(principal.getName())) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}

		if(v.removeLikedUser(principal.getName())) {
			v.setLikes(v.getLikes() - 1);
		}
		repository.save(v);

		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/likedby", method = RequestMethod.GET)
	public Collection<String> likedVideo(@PathVariable("id") long id) {
		Video v = repository.findOne(id);

		return v.getLikedUsers();
	}
}
