package com.edu.zzti.ass.management.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.edu.zzti.ass.core.model.PageInfo;
import com.edu.zzti.ass.management.model.TFile;
import com.edu.zzti.ass.management.model.TTeacher;
import com.edu.zzti.ass.management.service.IFileService;
import com.edu.zzti.ass.testlibrary.model.TUnit;

@Controller
@RequestMapping(value = "/manage/file")
public class FileManagementController {
	@Autowired
	private IFileService fileService;

	@RequestMapping(value = "pptManagement/{page}", method = { RequestMethod.GET })
	public ModelAndView pptView(Model model, @PathVariable Integer page,
			HttpSession httpSession) {

		ModelAndView mav = new ModelAndView();
		TTeacher teacher = (TTeacher) httpSession.getAttribute("userInfo");
		if (teacher.getIsAdmin() == 0) {
			mav.addObject("info", "对不起，你不是管理员老师！");
			mav.setViewName("manage/info");
			return mav;
		} else {
			List<TUnit> units = fileService.findUnit();
			mav.addObject("units", units);

			PageInfo<TFile> pageInfo = fileService.findByPage(page, 1);
			mav.addObject("pageInfo", pageInfo);

			mav.setViewName("manage/pptManagement");
			return mav;
		}

	}

	@RequestMapping(value = "pptDelete/{id}/{page}", method = { RequestMethod.GET })
	public ModelAndView pptDelete(Model model, HttpServletRequest request,
			@PathVariable Integer id, @PathVariable Integer page) {

		ModelAndView mav = new ModelAndView();
		String url = request.getServletContext().getRealPath("")
				.replace("\\", "/");
		fileService.pptDelete(id, url);

		mav.setViewName("redirect:/manage/file/pptManagement/" + page);
		return mav;
	}

	@RequestMapping(value = "videoManagement/{page}", method = { RequestMethod.GET })
	public ModelAndView videoView(Model model, @PathVariable Integer page,
			HttpSession httpSession) {

		ModelAndView mav = new ModelAndView();
		TTeacher teacher = (TTeacher) httpSession.getAttribute("userInfo");
		if (teacher.getIsAdmin() == 0) {
			mav.addObject("info", "对不起，你不是管理员老师！");
			mav.setViewName("manage/info");
			return mav;
		} else {
			List<TUnit> units = fileService.findUnit();
			mav.addObject("units", units);

			PageInfo<TFile> pageInfo = fileService.findByPage(page, 2);
			mav.addObject("pageInfo", pageInfo);
			mav.setViewName("manage/videoManagement");
			return mav;
		}
	}

	@RequestMapping(value = "videoDelete/{id}/{page}", method = { RequestMethod.GET })
	public ModelAndView videoDelete(Model model, HttpServletRequest request,
			@PathVariable Integer id, @PathVariable Integer page) {

		ModelAndView mav = new ModelAndView();
		String url = request.getServletContext().getRealPath("")
				.replace("\\", "/");
		fileService.pptDelete(id, url);

		mav.setViewName("redirect:/manage/file/videoManagement/" + page);
		return mav;
	}

	@RequestMapping(value = "documentManagement/{page}", method = { RequestMethod.GET })
	public ModelAndView documentView(Model model, @PathVariable Integer page,HttpSession httpSession) {

		ModelAndView mav = new ModelAndView();
		TTeacher teacher = (TTeacher) httpSession.getAttribute("userInfo");
		if (teacher.getIsAdmin() == 0) {
			mav.addObject("info", "对不起，你不是管理员老师！");
			mav.setViewName("manage/info");
			return mav;
		} else {
			List<TUnit> units = fileService.findUnit();
			mav.addObject("units", units);

			PageInfo<TFile> pageInfo = fileService.findByPage(page, 3);
			mav.addObject("pageInfo", pageInfo);
			mav.setViewName("manage/documentManagement");
			return mav;
		}

	}

	@RequestMapping(value = "documentDelete/{id}/{page}", method = { RequestMethod.GET })
	public ModelAndView documentDelete(Model model, HttpServletRequest request,
			@PathVariable Integer id, @PathVariable Integer page) {

		ModelAndView mav = new ModelAndView();
		String url = request.getServletContext().getRealPath("")
				.replace("\\", "/");
		fileService.pptDelete(id, url);

		mav.setViewName("redirect:/manage/file/documentManagement/" + page);
		return mav;
	}
}
