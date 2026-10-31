package seg3x02.tempconverterapi

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class TempConverterApiApplicationTests(@Autowired val mvc: MockMvc) {
    private val url = "/temperature-converter/celsius-fahrenheit/50"

    @Test fun sansIdentifiantsRefuse() { mvc.perform(get(url)).andExpect(status().isUnauthorized) }
    @Test fun user1Accepte() {
        mvc.perform(get(url).with(httpBasic("user1", "pass1"))).andExpect(status().isOk).andExpect(content().string("122.0"))
    }
    @Test fun user2Accepte() {
        mvc.perform(get("/temperature-converter/fahrenheit-celsius/122").with(httpBasic("user2", "pass2")))
            .andExpect(status().isOk).andExpect(content().string("50.0"))
    }
    @Test fun mauvaisMotDePasseRefuse() {
        mvc.perform(get(url).with(httpBasic("user1", "wrong"))).andExpect(status().isUnauthorized)
    }
}
