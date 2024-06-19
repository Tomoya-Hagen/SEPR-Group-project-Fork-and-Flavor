package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.search.DateTerm;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.text.SimpleDateFormat;
import java.util.Date;


@SpringBootTest
@ActiveProfiles({"test"})
@Transactional
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class WeekPlanTest implements TestData {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void getWeekPlanbyIDReturnAll() throws Exception {
        String sfrom = "2024-06-19";
        String sto = "2024-06-26";

        MvcResult mvcResult = this.mockMvc.perform(get(WEEKPLAN_BASE_URI + "/{id}", 1)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .param("from", "2024-06-19")
            .param("to", "2024-06-26"))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        WeekPlanDetailDto[] recipeDetailDto = objectMapper.readValue(response.getContentAsString(),
            WeekPlanDetailDto[].class);


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date from = formatter.parse(sfrom);
        Date to = formatter.parse(sto);

        assertEquals(recipeDetailDto.length, 3);

//        for (WeekPlanDetailDto dto : recipeDetailDto) {
//            assertTrue(dto.date.after(from));
//            assertTrue(dto.date.before(to));
//        }
    }

    @Test
    void getWeekPlanbyIDReturnEmpty() throws Exception {
        String sfrom = "2024-06-19";
        String sto = "2024-06-26";

        MvcResult mvcResult = this.mockMvc.perform(get(WEEKPLAN_BASE_URI + "/{id}", -1)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .param("from", "2024-06-19")
                .param("to", "2024-06-26"))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        WeekPlanDetailDto[] recipeDetailDto = objectMapper.readValue(response.getContentAsString(),
            WeekPlanDetailDto[].class);


        assertEquals(recipeDetailDto.length, 0);
    }
}
