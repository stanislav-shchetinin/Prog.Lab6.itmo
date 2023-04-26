package tests;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "src.test.java.step",
        tags = "@all",
        snippets = CucumberOptions.SnippetType.UNDERSCORE
)
public class RunnerTest {
}
