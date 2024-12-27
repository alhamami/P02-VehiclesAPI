package com.udacity.vehicles.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.service.CarService;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import static org.hamcrest.Matchers.hasSize;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

/**
 * Implements testing of the CarController class.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CarControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<Car> json;

    @MockBean
    private CarService carService;

    @MockBean
    private PriceClient priceClient;

    @MockBean
    private MapsClient mapsClient;

    /**
     * Creates pre-requisites for testing, such as an example car.
     */
    @Before
    public void setup() {
        Car car = getCar();
        car.setId(1L);
        given(carService.save(any())).willReturn(car);
        given(carService.findById(any())).willReturn(car);
        given(carService.list()).willReturn(Collections.singletonList(car));
    }

    /**
     * Tests for successful creation of new car in the system
     * @throws Exception when car creation fails in the system
     */
    @Test
    public void createCar() throws Exception {
        Car car = getCar();
        mvc.perform(
                post(new URI("/cars"))
                        .content(json.write(car).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    /**
     * Tests if the read operation appropriately returns a list of vehicles.
     * @throws Exception if the read operation of the vehicle list fails
     */
    @Test
    public void listCars() throws Exception {
        /**
         * TODO: Add a test to check that the `get` method works by calling
         *   the whole list of vehicles. This should utilize the car from `getCar()`
         *   below (the vehicle will be the first in the list).
         */

        MediaType mediaTypeAccept = MediaType.APPLICATION_JSON_UTF8;

        Matcher<Collection<?>> carSize = hasSize(1);
        Matcher<String> matchCarCreatedAt = is(String.valueOf(getCar().getCreatedAt()));
        Matcher<String> matchCarModifiedAt = is(String.valueOf(getCar().getModifiedAt()));
        Matcher<String> matchCarCondition = is(getCar().getCondition().toString());
        Matcher<String> matchCarBody = is(getCar().getDetails().getBody());
        Matcher<String> matchCarModel = is(getCar().getDetails().getModel());
        Matcher<String> matchCarManufacturerCode = is(String.valueOf(getCar().getDetails().getManufacturer().getCode()));
        Matcher<String> matchCarManufacturerName = is(String.valueOf(getCar().getDetails().getManufacturer().getName()));
        Matcher<Integer> matchDoorsNum = is(getCar().getDetails().getNumberOfDoors());
        Matcher<String> matchCarFuel = is(getCar().getDetails().getFuelType());
        Matcher<String> matchCarEngine = is(getCar().getDetails().getEngine());
        Matcher<Integer> matchCarMile = is(getCar().getDetails().getMileage());
        Matcher<Integer> matchCarModelYear = is(getCar().getDetails().getModelYear());
        Matcher<Integer> matchCarProdYear = is(getCar().getDetails().getProductionYear());
        Matcher<String> matchCarColor = is(getCar().getDetails().getExternalColor());
        Matcher<Double> matchCarLon = is(getCar().getLocation().getLon());
        Matcher<Double> matchCarLat = is(getCar().getLocation().getLat());
        Matcher<Double> matchCarAddress = is(Double.valueOf(getCar().getLocation().getAddress()));
        Matcher<Double> matchCarCity = is(Double.valueOf(getCar().getLocation().getCity()));
        Matcher<Double> matchCarState = is(Double.valueOf(getCar().getLocation().getState()));
        Matcher<Double> matchCarZip = is(Double.valueOf(getCar().getLocation().getZip()));
        Matcher<Double> matchCarPrice = is(Double.valueOf(getCar().getPrice()));



        ResultMatcher checkCarSize = jsonPath("$._embedded.carList", carSize);
        ResultMatcher checkCarNotEmpty = jsonPath("$._embedded.carList[0]").exists();
        ResultMatcher checkCarId = jsonPath("$._embedded.carList[0].id").value(1);
        ResultMatcher checkCarCreatedAt = jsonPath("$._embedded.carList[0].createdAt").value( matchCarCreatedAt);
        ResultMatcher checkCarModifiedAt = jsonPath("$._embedded.carList[0].modifiedAt").value(matchCarModifiedAt);
        ResultMatcher checkCarCondition = jsonPath("$._embedded.carList[0].condition").value( matchCarCondition);
        ResultMatcher checkCarBody = jsonPath("$._embedded.carList[0].details.body").value( matchCarBody);
        ResultMatcher checkCarModel = jsonPath("$._embedded.carList[0].details.model").value( matchCarModel);
        ResultMatcher checkCarManufacturerCode = jsonPath("$._embedded.carList[0].details.manufacturer.code").value( matchCarManufacturerCode);
        ResultMatcher checkCarManufacturerName = jsonPath("$._embedded.carList[0].details.manufacturer.name").value( matchCarManufacturerName);
        ResultMatcher checkCarDoorsNum = jsonPath("$._embedded.carList[0].details.numberOfDoors").value( matchDoorsNum);
        ResultMatcher checkCarFuel = jsonPath("$._embedded.carList[0].details.fuelType").value( matchCarFuel);
        ResultMatcher checkCarEngine = jsonPath("$._embedded.carList[0].details.engine").value( matchCarEngine);
        ResultMatcher checkCarMile = jsonPath("$._embedded.carList[0].details.mileage").value( matchCarMile);
        ResultMatcher checkCarYear = jsonPath("$._embedded.carList[0].details.modelYear").value( matchCarModelYear);
        ResultMatcher checkCarProdYear = jsonPath("$._embedded.carList[0].details.productionYear").value( matchCarProdYear);
        ResultMatcher checkCarColor = jsonPath("$._embedded.carList[0].details.externalColor").value( matchCarColor);
        ResultMatcher checkCarLocLon = jsonPath("$._embedded.carList[0].location.lon").value( matchCarLon);
        ResultMatcher checkCarLocLat = jsonPath("$._embedded.carList[0].location.lat").value( matchCarLat);
        ResultMatcher checkCarAddress = jsonPath("$._embedded.carList[0].location.address").value( matchCarAddress);
        ResultMatcher checkCarCity = jsonPath("$._embedded.carList[0].location.city").value( matchCarCity);
        ResultMatcher checkCarState = jsonPath("$._embedded.carList[0].location.state").value( matchCarState);
        ResultMatcher checkCarZip = jsonPath("$._embedded.carList[0].location.zip").value( matchCarZip);
        ResultMatcher checkCarPrice = jsonPath("$._embedded.carList[0].location.zip").value( matchCarPrice);


        mvc.perform(get("/cars").accept(mediaTypeAccept)).andExpect(status().isOk())
                .andExpect(checkCarSize)
                .andExpect(checkCarNotEmpty)
                .andExpect(checkCarId)
                .andExpect(checkCarCreatedAt)
                .andExpect(checkCarModifiedAt)
                .andExpect(checkCarCondition)
                .andExpect(checkCarBody)
                .andExpect(checkCarModel)
                .andExpect(checkCarManufacturerCode)
                .andExpect(checkCarManufacturerName)
                .andExpect(checkCarDoorsNum)
                .andExpect(checkCarFuel)
                .andExpect(checkCarEngine)
                .andExpect(checkCarMile)
                .andExpect(checkCarYear)
                .andExpect(checkCarProdYear)
                .andExpect(checkCarColor)
                .andExpect(checkCarLocLon)
                .andExpect(checkCarLocLat)
                .andExpect(checkCarAddress)
                .andExpect(checkCarCity)
                .andExpect(checkCarState)
                .andExpect(checkCarZip)
                .andExpect(checkCarPrice);

    }

    /**
     * Tests the read operation for a single car by ID.
     * @throws Exception if the read operation for a single car fails
     */
    @Test
    public void findCar() throws Exception {
        /**
         * TODO: Add a test to check that the `get` method works by calling
         *   a vehicle by ID. This should utilize the car from `getCar()` below.
         */

        Car car = getCar();

        MediaType mediaTypeAccept = MediaType.APPLICATION_JSON_UTF8;

        Matcher<Collection<?>> carSize = hasSize(1);
        Long matchCarId = car.getId();
        String matchCarCreatedAt = String.valueOf(car.getCreatedAt());
        LocalDateTime matchCarModifiedAt = car.getModifiedAt();
        Matcher<String> matchCarCondition = is(getCar().getCondition().toString());
        String matchCarBody = car.getDetails().getBody();
        String matchCarModel = car.getDetails().getModel();
        Integer matchCarManufacturerCode = car.getDetails().getManufacturer().getCode();
        String matchCarManufacturerName = car.getDetails().getManufacturer().getName();
        Integer matchDoorsNum = car.getDetails().getNumberOfDoors();
        String matchCarFuel = car.getDetails().getFuelType();
        String matchCarEngine = car.getDetails().getEngine();
        Integer matchCarMile = car.getDetails().getMileage();
        Integer matchCarModelYear = car.getDetails().getModelYear();
        Integer matchCarProdYear = car.getDetails().getProductionYear();
        String matchCarColor = car.getDetails().getExternalColor();
        Double matchCarLon = car.getLocation().getLon();
        Double matchCarLat = car.getLocation().getLat();
        String matchCarAddress = car.getLocation().getAddress();
        String matchCarCity = car.getLocation().getCity();
        String matchCarState = car.getLocation().getState();
        String matchCarZip = car.getLocation().getZip();
        String matchCarPrice = car.getPrice();



        ResultMatcher checkCarSize = jsonPath("$._embedded.carList", carSize);
        ResultMatcher checkCarNotEmpty = jsonPath("$._embedded.carList[0]").exists();
        ResultMatcher checkCarId = jsonPath("$.carList[0].id").value(matchCarId);
        ResultMatcher checkCarCreatedAt = jsonPath("$.carList[0].createdAt").value( matchCarCreatedAt);
        ResultMatcher checkCarModifiedAt = jsonPath("$.carList[0].modifiedAt").value(matchCarModifiedAt);
        ResultMatcher checkCarCondition = jsonPath("$.carList[0].condition").value( matchCarCondition);
        ResultMatcher checkCarBody = jsonPath("$.carList[0].details.body").value( matchCarBody);
        ResultMatcher checkCarModel = jsonPath("$.carList[0].details.model").value( matchCarModel);
        ResultMatcher checkCarManufacturerCode = jsonPath("$.carList[0].details.manufacturer.code").value( matchCarManufacturerCode);
        ResultMatcher checkCarManufacturerName = jsonPath("$.carList[0].details.manufacturer.name").value( matchCarManufacturerName);
        ResultMatcher checkCarDoorsNum = jsonPath("$.carList[0].details.numberOfDoors").value( matchDoorsNum);
        ResultMatcher checkCarFuel = jsonPath("$.carList[0].details.fuelType").value( matchCarFuel);
        ResultMatcher checkCarEngine = jsonPath("$.carList[0].details.engine").value( matchCarEngine);
        ResultMatcher checkCarMile = jsonPath("$.carList[0].details.mileage").value( matchCarMile);
        ResultMatcher checkCarYear = jsonPath("$.carList[0].details.modelYear").value( matchCarModelYear);
        ResultMatcher checkCarProdYear = jsonPath("$.carList[0].details.productionYear").value( matchCarProdYear);
        ResultMatcher checkCarColor = jsonPath("$.carList[0].details.externalColor").value( matchCarColor);
        ResultMatcher checkCarLocLon = jsonPath("$.carList[0].location.lon").value( matchCarLon);
        ResultMatcher checkCarLocLat = jsonPath("$.carList[0].location.lat").value( matchCarLat);
        ResultMatcher checkCarAddress = jsonPath("$.carList[0].location.address").value( matchCarAddress);
        ResultMatcher checkCarCity = jsonPath("$.carList[0].location.city").value( matchCarCity);
        ResultMatcher checkCarState = jsonPath("$.carList[0].location.state").value( matchCarState);
        ResultMatcher checkCarZip = jsonPath("$.carList[0].location.zip").value( matchCarZip);
        ResultMatcher checkCarPrice = jsonPath("$.carList[0].location.zip").value( matchCarPrice);


        mvc.perform(get("/cars/{id}", car.getId()).accept(mediaTypeAccept)).andExpect(status().isOk())
                .andExpect(checkCarSize)
                .andExpect(checkCarNotEmpty)
                .andExpect(checkCarId)
                .andExpect(checkCarCreatedAt)
                .andExpect(checkCarModifiedAt)
                .andExpect(checkCarCondition)
                .andExpect(checkCarBody)
                .andExpect(checkCarModel)
                .andExpect(checkCarManufacturerCode)
                .andExpect(checkCarManufacturerName)
                .andExpect(checkCarDoorsNum)
                .andExpect(checkCarFuel)
                .andExpect(checkCarEngine)
                .andExpect(checkCarMile)
                .andExpect(checkCarYear)
                .andExpect(checkCarProdYear)
                .andExpect(checkCarColor)
                .andExpect(checkCarLocLon)
                .andExpect(checkCarLocLat)
                .andExpect(checkCarAddress)
                .andExpect(checkCarCity)
                .andExpect(checkCarState)
                .andExpect(checkCarZip)
                .andExpect(checkCarPrice);

    }

    /**
     * Tests the deletion of a single car by ID.
     * @throws Exception if the delete operation of a vehicle fails
     */
    @Test
    public void deleteCar() throws Exception {
        /**
         * TODO: Add a test to check whether a vehicle is appropriately deleted
         *   when the `delete` method is called from the Car Controller. This
         *   should utilize the car from `getCar()` below.
         */

        Car deletedCar = getCar();

        mvc.perform(delete("/cars/{id}", deletedCar.getId())).andExpect(status().isNoContent());
    }

    /**
     * Creates an example Car object for use in testing.
     * @return an example Car object
     */
    private Car getCar() {
        Car car = new Car();
        car.setLocation(new Location(40.730610, -73.935242));
        Details details = new Details();
        Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
        details.setManufacturer(manufacturer);
        details.setModel("Impala");
        details.setMileage(32280);
        details.setExternalColor("white");
        details.setBody("sedan");
        details.setEngine("3.6L V6");
        details.setFuelType("Gasoline");
        details.setModelYear(2018);
        details.setProductionYear(2018);
        details.setNumberOfDoors(4);
        car.setDetails(details);
        car.setCondition(Condition.USED);
        return car;
    }
}