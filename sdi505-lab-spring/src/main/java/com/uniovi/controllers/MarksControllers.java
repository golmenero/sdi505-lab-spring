package com.uniovi.controllers;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.uniovi.entities.Mark;
import com.uniovi.entities.User;
import com.uniovi.services.MarkService;
import com.uniovi.services.UsersService;

@Controller
public class MarksControllers {

	@Autowired
	private HttpSession httpSession;

	@Autowired // Inyectar el servicio
	private MarkService marksService;

	@Autowired
	private UsersService usersService;

	@RequestMapping("/mark/list")
	public String getList(Model model, Principal principal,
			@RequestParam(value = "", required = false) String searchText) {
		String dni = principal.getName(); // DNI es el name de la autenticación
		User user = usersService.getUserByDni(dni);
		if (searchText != null && !searchText.isEmpty()) {
			model.addAttribute("markList", marksService.searchMarksByDescriptionAndNameForUser(searchText, user));
		} else {
			model.addAttribute("markList", marksService.getMarksForUser(user));
		}
		return "mark/list";
	}

	@RequestMapping(value = "/mark/add", method = RequestMethod.POST)
	public String setMark(@ModelAttribute Mark mark) {
		marksService.addMark(mark);
		return "redirect:/mark/list";
	}

	@RequestMapping("/mark/details/{id}")
	public String getDetail(Model model, @PathVariable Long id) {
		model.addAttribute("mark", marksService.getMark(id));
		return "mark/details";
	}

	@RequestMapping("/mark/delete/{id}")
	public String deleteMark(@PathVariable Long id) {
		marksService.deleteMark(id);
		return "redirect:/mark/list";
	}

	@RequestMapping(value = "/mark/add")
	public String getMark(Model model) {
		model.addAttribute("usersList", usersService.getUsers());
		return "mark/add";
	}

	@RequestMapping(value = "/mark/edit/{id}")
	public String getEdit(Model model, @PathVariable Long id) {
		model.addAttribute("mark", marksService.getMark(id));
		model.addAttribute("usersList", usersService.getUsers());
		return "mark/edit";
	}

	@RequestMapping(value = "/mark/edit/{id}", method = RequestMethod.POST)
	public String setEdit(Model model, @PathVariable Long id, @ModelAttribute Mark mark) {
		Mark original = marksService.getMark(id);
		// modificar solo score y description
		original.setScore(mark.getScore());
		original.setDescription(mark.getDescription());
		marksService.addMark(original);
		return "redirect:/mark/details/" + id;
	}

	@RequestMapping(value = "/mark/{id}/resend", method = RequestMethod.GET)
	public String setResendTrue(Model model, @PathVariable Long id) {
		marksService.setMarkResend(true, id);
		return "redirect:/mark/list";
	}

	@RequestMapping(value = "/mark/{id}/noresend", method = RequestMethod.GET)
	public String setResendFalse(Model model, @PathVariable Long id) {
		marksService.setMarkResend(false, id);
		return "redirect:/mark/list";
	}

	@RequestMapping("/mark/list/update")
	public String updateList(Model model, Principal principal) {
		String dni = principal.getName(); // DNI es el name de la autenticación
		User user = usersService.getUserByDni(dni);
		model.addAttribute("markList", marksService.getMarksForUser(user));
		return "mark/list :: tableMarks";
	}

}
