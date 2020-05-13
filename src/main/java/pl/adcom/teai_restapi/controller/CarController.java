package pl.adcom.teai_restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.adcom.teai_restapi.model.Car;
import pl.adcom.teai_restapi.model.Color;
import pl.adcom.teai_restapi.service.CarService;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/cars", produces = {
        MediaType.APPLICATION_XML_VALUE,
        MediaType.APPLICATION_JSON_VALUE })
public class CarController {

    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping()
    public ResponseEntity<CollectionModel<Car>> getAllCars(){
        List<Car> allCars = carService.findAllCars();

        if(!allCars.isEmpty()) {
            allCars.forEach(car -> car.addIf(!car.hasLinks(), () -> linkTo(CarController.class).slash(car.getId()).withSelfRel()));
            Link link = linkTo(CarController.class).withSelfRel();
            CollectionModel<Car> carResources = new CollectionModel<>(allCars, link);
            return new ResponseEntity<>(carResources, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getAllCars(@PathVariable long id){

        Link link = linkTo(CarController.class).slash(id).withSelfRel();

        Optional<Car> car = carService.findCarById(id);
        EntityModel<Car> carResource = new EntityModel<>(car.get(), link);

        if (car.isPresent()){
            return new ResponseEntity(carResource, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/color/{color}")
    public ResponseEntity<CollectionModel<Car>> getCarsByColor(@PathVariable String color){

        List<Car> carList = carService.findCarByColor(Color.valueOf(color.toUpperCase()));

        if (!carList.isEmpty()) {
            carList.forEach(car -> car.add(linkTo(CarController.class).slash(car.getId()).withSelfRel()));
            carList.forEach(car -> car.add(linkTo(CarController.class).withRel("allColors")));
            Link link = linkTo(CarController.class).withSelfRel();

            CollectionModel<Car> carResources = new CollectionModel<>(carList, link);
            return new ResponseEntity<>(carResources, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping()
    public ResponseEntity addNewCar(@Validated @RequestBody Car car){

        boolean isAdded = carService.addCar(car);

        if (isAdded) {
            return new ResponseEntity<>("Nowy pojazd został dodany ", HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping()
    public ResponseEntity modCar(@Validated @RequestBody Car car) {
        Optional<Car> modCar = carService.modifyCar(car);

        if (modCar.isPresent()) {
            return new ResponseEntity<>("Zmodyfikowano pojazd o id " + modCar.get().getId(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity modColorCarById(@Validated @RequestParam Color color,
                                          @PathVariable long id) {
        Optional<Car> modColorCar = carService.changeColorCarById(color, id);

        if(modColorCar.isPresent()) {
            return new ResponseEntity<>("Zmodyfikowano kolor pojazdu o id " + modColorCar.get().getId(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removeCar(@PathVariable long id) {
        Optional<Car> delCar = carService.removeCarById(id);

        if (delCar.isPresent()) {
            return new ResponseEntity("Pojazd o id " + delCar.get().getId() + " został usunięty", HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
