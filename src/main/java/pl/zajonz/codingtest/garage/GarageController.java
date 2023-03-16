package pl.zajonz.codingtest.garage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/garages")
public class GarageController {

    private final GarageService garageService;

    @GetMapping
    public String getGarageList(Model model){
        model.addAttribute("garages", garageService.findAll());
        return "list";
    }
}
