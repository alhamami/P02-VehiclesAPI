package com.udacity.vehicles.api;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.service.CarService;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiParam;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




/**
 * Implements a REST-based controller for the Vehicles API.
 */


@RestController
@RequestMapping("/cars")
class CarController {

    private final CarService carService;
    private final CarResourceAssembler assembler;

    CarController(CarService carService, CarResourceAssembler assembler) {
        this.carService = carService;
        this.assembler = assembler;
    }

    /**
     * Creates a list to store any vehicles.
     * @return list of vehicles
     */
    @ApiOperation(value = "Creates a list to store any vehicles", notes = "Creates a list to store any vehicles and return list of vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized Request"),
            @ApiResponse(code = 403, message = "Forbidden Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @GetMapping
    Resources<Resource<Car>> list() {
        List<Resource<Car>> resources = carService.list().stream().map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(resources,
                linkTo(methodOn(CarController.class).list()).withSelfRel());
    }

    /**
     * Gets information of a specific car by ID.
     * @param id the id number of the given vehicle
     * @return all information for the requested vehicle
     */
    @ApiOperation(value = "Gets information of a specific car by ID", notes = "Gets information of a specific car by ID and return all information for the requested vehicle")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized Request"),
            @ApiResponse(code = 403, message = "Forbidden Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @GetMapping("/{id}")
    Resource<Car> get(@ApiParam(value  = "id is the id number of the given vehicle") @PathVariable Long id) {
        /**
         * TODO: Use the `findById` method from the Car Service to get car information.
         * TODO: Use the `assembler` on that car and return the resulting output.
         *   Update the first line as part of the above implementing.
         */
        Car foundCar = carService.findById(id);
        Resource<Car> resource = assembler.toResource(foundCar);

        return resource;
    }

    /**
     * Posts information to create a new vehicle in the system.
     * @param car A new vehicle to add to the system.
     * @return response that the new vehicle was added to the system
     * @throws URISyntaxException if the request contains invalid fields or syntax
     */
    @ApiOperation(value = "Posts information to create a new vehicle in the system", notes = "Posts information to create a new vehicle in the system and response that the new vehicle was added to the system")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized Request"),
            @ApiResponse(code = 403, message = "Forbidden Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @PostMapping
    ResponseEntity<?> post(@ApiParam(value = "car is a new vehicle to add to the system") @Valid @RequestBody Car car) throws URISyntaxException {
        /**
         * TODO: Use the `save` method from the Car Service to save the input car.
         * TODO: Use the `assembler` on that saved car and return as part of the response.
         *   Update the first line as part of the above implementing.
         */
        Car newCar = carService.save(car);
        Resource<Car> resource = assembler.toResource(newCar);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    /**
     * Updates the information of a vehicle in the system.
     * @param id The ID number for which to update vehicle information.
     * @param car The updated information about the related vehicle.
     * @return response that the vehicle was updated in the system
     */
    @ApiOperation(value = "Updates the information of a vehicle in the system", notes = "Updates the information of a vehicle in the system and response that the vehicle was updated in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized Request"),
            @ApiResponse(code = 403, message = "Forbidden Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @PutMapping("/{id}")
    ResponseEntity<?> put(@ApiParam(value = "id is the ID number for which to update vehicle information") @PathVariable Long id,@ApiParam(value = "car is the updated information about the related vehicle") @Valid @RequestBody Car car) {
        /**
         * TODO: Set the id of the input car object to the `id` input.
         * TODO: Save the car using the `save` method from the Car service
         * TODO: Use the `assembler` on that updated car and return as part of the response.
         *   Update the first line as part of the above implementing.
         */

        Car upadteCar = new Car();
        upadteCar.setId(id);
        upadteCar.setCreatedAt(car.getCreatedAt());
        upadteCar.setModifiedAt(car.getModifiedAt());
        upadteCar.setCondition(car.getCondition());
        upadteCar.setDetails(car.getDetails());
        upadteCar.setLocation(upadteCar.getLocation());
        upadteCar.setPrice(car.getPrice());

        Car updatedCar = carService.save(car);

        Resource<Car> resource = assembler.toResource(updatedCar);
        return ResponseEntity.ok(resource);
    }

    /**
     * Removes a vehicle from the system.
     * @param id The ID number of the vehicle to remove.
     * @return response that the related vehicle is no longer in the system
     */
    @ApiOperation(value = "Removes a vehicle from the system", notes = "Removes a vehicle from the system and response that the related vehicle is no longer in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized Request"),
            @ApiResponse(code = 403, message = "Forbidden Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@ApiParam(value = "id is the ID number of the vehicle to remove") @PathVariable Long id) {
        /**
         * TODO: Use the Car Service to delete the requested vehicle.
         */

        carService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
