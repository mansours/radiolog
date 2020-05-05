package com.example.catalog.dto;

import com.example.catalog.persistence.entities.Album;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.*;

import java.util.Set;

import static com.example.catalog.TestConstants.ALBUM_ADELE_21;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
public class AlbumDTOTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeClass
    public static void createValidator(){
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    @AfterClass
    public static void close() { validatorFactory.close(); }

    @Test
    public void testEmptyName(){
             Album obj = ALBUM_ADELE_21();
             AlbumDTO dto = new AlbumDTO(obj);

             dto.setName("");
             Set<ConstraintViolation<AlbumDTO>> violations = validator.validate(dto);
             assertEquals( 1, violations.size());

    }
    @Test
    public void testZeroTracks(){
        Album obj = ALBUM_ADELE_21();
        AlbumDTO dto = new AlbumDTO(obj);
        dto.setNumberOfTracks(0L);

        Set<ConstraintViolation<AlbumDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());

    }
}


