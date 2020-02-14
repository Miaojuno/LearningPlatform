package xcj.hs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserControllerTest {

  private MockMvc mockMvc;
  protected ObjectMapper objectMapper = new ObjectMapper();
  @Autowired private UserController userController;

  @Rule
  public JUnitRestDocumentation restDocumentation =
      new JUnitRestDocumentation("target/generated-snippets");

  @Test
  public void findbyid() throws Exception {
    mockMvc =
        MockMvcBuilders.standaloneSetup(userController)
            .apply(documentationConfiguration(restDocumentation))
            .alwaysDo(
                document(
                    "userController/findById",
                    relaxedResponseFields(
                        subsectionWithPath("success").description("success").type("boolean"),
                        subsectionWithPath("data.userId").description("userId").type("String"),
                        subsectionWithPath("data.userAccount")
                            .description("userAccount")
                            .type("String"),
                        subsectionWithPath("data.userName").description("userName").type("String"),
                        subsectionWithPath("data.userPwd").description("userPwd").type("String"),
                        subsectionWithPath("data.roleId").description("roleId").type("String"),
                        subsectionWithPath("data.roleName").description("roleName").type("String"),
                        subsectionWithPath("data.isActive").description("isActive").type("String"),
                        subsectionWithPath("data.superiorId")
                            .description("superiorId")
                            .type("String"),
                        subsectionWithPath("data.superiorName")
                            .description("data.superiorName")
                            .type("String"),
                        subsectionWithPath("data.superiorAccount")
                            .description("data.superiorAccount")
                            .type("String"),
                        subsectionWithPath("data.userDesc").description("userDesc").type("String"),
                        subsectionWithPath("data.grade").description("grade").type("String"))))
            .build();

    Map<String, Object> note = new HashMap<>();
    note.put("userId", "8a8181816e156569016e156979230003");

    MvcResult result =
        mockMvc
            .perform(
                post("/user/findbyid", 1)
                    .param("userId", "8a8181816e156569016e156979230003")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(note)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andDo(
                document(
                    "userController/findById",
                    relaxedRequestFields(fieldWithPath("userId").description("userId"))))
            .andReturn();
  }

  @Test
  public void listUser() throws Exception {
    mockMvc =
        MockMvcBuilders.standaloneSetup(userController)
            .apply(documentationConfiguration(restDocumentation))
            .alwaysDo(
                document(
                    "userController/pageList.json",
                    relaxedResponseFields(
                        subsectionWithPath("data").description("data").type("object"),
                        subsectionWithPath("total").description("total").type("object"))))
            .build();

    Map<String, Object> note = new HashMap<>();
    note.put("pageSize", 5);
    note.put("pageNumber", 1);
    note.put("userName", "");
    note.put("roleId", "");
    note.put("superiorAccount", "");

    MvcResult result =
        mockMvc
            .perform(
                post("/user/pageList.json")
                    .accept(MediaType.APPLICATION_JSON)
                    .param("pageSize", "5")
                    .param("pageNumber", "1")
                    .param("userName", "")
                    .param("roleId", "")
                    .param("superiorAccount", "")
                    .contentType("application/json;charset=utf-8")
                    .content(this.objectMapper.writeValueAsString(note)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andDo(
                document(
                    "userController/pageList.json",
                    relaxedRequestFields(fieldWithPath("pageSize").description("pageSize")),
                    relaxedRequestFields(fieldWithPath("pageNumber").description("pageNumber")),
                    relaxedRequestFields(fieldWithPath("userName").description("userName")),
                    relaxedRequestFields(fieldWithPath("roleId").description("roleId")),
                    relaxedRequestFields(
                        fieldWithPath("superiorAccount").description("superiorAccount"))))
            .andReturn();
  }
}
