package org.magnum.dataup.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

import org.magnum.dataup.VideoSvcApi;
import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import retrofit.mime.TypedFile;

@Controller
public class VideoSvcController {
	private Map<Long,Video> videos = new HashMap<Long, Video>();
	private static final AtomicLong id = new AtomicLong(0L);
	 
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

	public VideoStatus setVideoData(long id, MultipartFile videoData) {
		// TODO Auto-generated method stub
		return null;
	}

	public void getData(long id) {
		// TODO Auto-generated method stub
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
