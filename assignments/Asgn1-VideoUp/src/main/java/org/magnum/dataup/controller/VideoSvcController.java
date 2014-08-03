package org.magnum.dataup.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.mime.TypedFile;

@Controller
public class VideoSvcController implements VideoSvcApi {
	 private List<Video> videos = new ArrayList<Video>();
	 private AtomicLong id = new AtomicLong(0L);
	 
	@RequestMapping(value=VIDEO_SVC_PATH, method=RequestMethod.GET)
	@GET(VIDEO_SVC_PATH)
	public @ResponseBody Collection<Video> getVideoList() {
		return videos;
	}

	@RequestMapping(value=VIDEO_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody Video addVideo(@RequestBody Video v, HttpServletRequest request) {
		v.setId(getNextId(1));
		v.setDataUrl(request.getRequestURL() + "/" + String.valueOf(v.getId()) + "/data");
		videos.add(v);
		return v;
	}

	@Override
	public VideoStatus setVideoData(long id, TypedFile videoData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getData(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	// Simple ID Generator 
	private long getNextId(int granularity) {
		return id.getAndAdd(granularity);
	}

	@Override
	public Video addVideo(Video v) {
		// TODO Auto-generated method stub
		return null;
	}

}
