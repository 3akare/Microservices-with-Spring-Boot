package com.microservice.cards.controller;

import com.microservice.cards.constant.CardsConstants;
import com.microservice.cards.dto.BuildVersionDto;
import com.microservice.cards.dto.CardsContactDto;
import com.microservice.cards.dto.CardsDto;
import com.microservice.cards.dto.ResponseDto;
import com.microservice.cards.service.ICardsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CRUD REST APIs Cards in bank",
        description = "CRUD REST APIs in bank to CREATE, READ, UPDATE and DELETE cards details"
)
@RestController
@RequestMapping(value = "/api/v1/cards", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class CardsController {
    private final ICardsService iCardService;
    private final Environment environment;
    private final CardsContactDto cardsContactDto;
    private final BuildVersionDto buildVersionDto;

    public CardsController(ICardsService iCardService, Environment environment, CardsContactDto cardsContactDto, BuildVersionDto buildVersionDto) {
        this.iCardService = iCardService;
        this.environment = environment;
        this.cardsContactDto = cardsContactDto;
        this.buildVersionDto = buildVersionDto;
    }

    @Operation(
            summary = "Create Card REST API",
            description = "REST APIs to create new Card inside bank"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status CREATED"
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCard(
            @RequestParam("mobile_number")
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
            String mobileNumber
    ){
        /* create card */
        iCardService.createCard(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ResponseDto(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201)
                );
    }

    @Operation(
            summary = "Fetch Card Details REST API",
            description = "REST APIs to fetch Card details based on mobile number"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @GetMapping
    public ResponseEntity<CardsDto> fetchCardDetails(
            @RequestParam("mobile_number")
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
            String mobileNumber
    ){
        /* get card details */
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(iCardService.fetchCard(mobileNumber));
    }

    @Operation(
            summary = "Update Card Details REST API",
            description = "REST APIs to update Card details based on mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status INTERNAL SERVER ERROR"
            )
    })
    @PatchMapping("/update")
    public ResponseEntity<ResponseDto> updateCardDetails(
            @Valid @RequestBody CardsDto cardsDto
    ){
        /* update card details */
        boolean isUpdated = iCardService.updateCard(cardsDto);
        if(isUpdated)
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200)
            );
        return ResponseEntity.status((HttpStatus.INTERNAL_SERVER_ERROR)).body(
                new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200)
            );
    }

    @Operation(
            summary = "Delete Card REST API",
            description = "REST APIs to delete Card details based on mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                responseCode = "200",
                description = "HTTP Status OK"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "HTTP Status INTERNAL SERVER ERROR"
            )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCard(
            @RequestParam("mobile_number")
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
            String mobileNumber
    ){
        /* delete card */
        boolean isDeleted = iCardService.deleteCard(mobileNumber);
        if(isDeleted)
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200)
            );
        return ResponseEntity.status((HttpStatus.INTERNAL_SERVER_ERROR)).body(
                new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200)
            );
    }

    @Operation(
            summary = "Fetch Java Version",
            description = "REST APIs to fetch Java version"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion(){
        /* get java version */
        return ResponseEntity.status(HttpStatus.OK).body(
                environment.getProperty("JAVA_HOME")
        );
    }

    @Operation(
            summary = "Fetch Build Version",
            description = "REST APIs to fetch Build version"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @GetMapping("/build-version")
    public ResponseEntity<BuildVersionDto> getBuildVersion(){
        /* get java version */
        return ResponseEntity.status(HttpStatus.OK).body(
                buildVersionDto
        );
    }

    @Operation(
            summary = "Fetch Contact Info",
            description = "REST APIs to fetch Contact Info"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @GetMapping("/contact-info")
    public ResponseEntity<CardsContactDto> getContactInfo(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(cardsContactDto);
    }
}
