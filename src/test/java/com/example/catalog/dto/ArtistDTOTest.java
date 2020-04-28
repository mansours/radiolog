package com.example.catalog.dto;

import com.example.catalog.persistence.entities.Artist;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static com.example.catalog.TestConstants.ARTIST_ADELE;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class ArtistDTOTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void testEmptyName() {
        Artist obj = ARTIST_ADELE();
        ArtistDTO dto = new ArtistDTO(obj);

        dto.setName("");

        Set<ConstraintViolation<ArtistDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
    }
}
