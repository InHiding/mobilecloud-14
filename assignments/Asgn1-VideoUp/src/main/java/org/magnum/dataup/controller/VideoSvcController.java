package org.magnum.dataup.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.magnum.dataup.VideoFileManager;
import org.magnum.dataup.VideoNotFoundException;
import org.magnum.dataup.VideoSvcApi;
import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import retrofit.mime.TypedFile;

@Controller
public class VideoSvcController {
	private Map<Long,Video> videos = new HashMap<Long, Video>();
	private static final AtomicLong id = new AtomicLong(0L);
	private VideoFileManager videoDataMgr;
	
	@PostConstruct
	public void init() throws IOException {
		videoDataMgr = VideoFileManager.get();
	}
	
	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Video> getVideoList() {
		return videos.values();
	}

	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody Video addVideo(@RequestBody Video v, HttpServletRequest request) {
		v.setId(getNextId(1));
		v.setDataUrl(getDataUrl(v.getId(), request));
		videos.put(v.getId(), v);
		return v;
	}

	@RequestMapping(value=VideoSvcApi.VIDEO_DATA_PATH, method=RequestMethod.POST)
	public @ResponseBody VideoStatus setVideoData(@PathVariable("id") long id, @RequestParam("data") MultipartFile videoData) throws IOException {
		Video video = videos.get(id);
		if (video == null) throw new VideoNotFoundException("Cannot findo video with id:" +id);
		
		videoDataMgr.saveVideoData(video, videoData.getInputStream());
		
		return new VideoStatus(VideoStatus.VideoState.READY);
	}

	@RequestMapping(value=VideoSvcApi.VIDEO_DATA_PATH, method=RequestMethod.GET)
	public void getData(@PathVariable("id") long id, HttpServletResponse response) throws IOException {
		Video video = videos.get(id);
		if (video == null) throw new VideoNotFoundException("Cannot findo video with id:" +id);
		
		videoDataMgr.copyVideoData(video, response.getOutputStream());
	}
	
	// Simple ID Generator 
	private long getNextId(int granularity) {
		return id.getAndAdd(granularity);
	}
	
	private String getDataUrl(long videoId, HttpServletRequest request){
        String url = request.getRequestURL() + "/" + String.valueOf(videoId) + "/data";
        return url;
    }
}
