package ru.cyri.webappnew.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.cyri.webappnew.dao.PersonDAO;
import ru.cyri.webappnew.models.Person;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO; //можем полуать людей из условной бд

    public PeopleController(PersonDAO personDAO) { //внедрение
        this.personDAO = personDAO;
    }
    @GetMapping()
    public String index(Model model) {
        // получ. всех людей из DAO и передадим на отображение в представление
        model.addAttribute("people", personDAO.index());
        return "people/index"; //шаблон отображающий список людей
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        // Получ. одного человека по id из DAO, передадим на отображение в представление
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }

    @GetMapping("/new") //вернется по гет запросу форма в браузер для нового человека
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()//получаем данные из формы,создаем новго человека,ложим в человека данные из формы, добавляем чела в бд
    public String create(@ModelAttribute("person") Person person) {
        personDAO.save(person); //метод save в DAO реализвуем
        return "redirect:/people"; //переход на др страницу (когда добавили человека в бд, браузер идет на стр со всеми людьми)
    }

    @GetMapping("/{id}/edit") //возвр страницу для редакта чела, внедр модель, извлекаем айди, в модель кидаем текущего чела

    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personDAO.show(id));
        return "people/edit"; //это представление имеет доступ к нашей модели
    }

    @PatchMapping("/{id}") //апдейт, принимаем обьект person из формы, принимаем айди из адреса
    public String update(@ModelAttribute("person") Person person, @PathVariable("id") int id) {
        personDAO.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }
}
