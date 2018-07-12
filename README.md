# rest-contract
_Reference_: [Rest-Contract](https://spring.io/guides/gs/contract-rest/)  
_Reference_: [Spring Cloud Contract](https://cloud.spring.io/spring-cloud-contract/)

# preface
The main goal of this project is to show how to create a Spring REST 
application with its contract stubs and consuming the contract within an 
other Spring application.

When trying to test an application that communicates with other services 
then we could do one of two things:

* deploy all microservices and perform end to end tests,

* mock other microservices in unit / integration tests.

To solve the second issues `Spring Cloud Contract Verifier` with 
`Stub Runner` were created. Their main idea is to give you very fast 
feedback, without the need to set up the whole world of microservices.

# Spring Cloud Contract Verifier features

* Ensure that HTTP / Messaging stubs (used when developing the client) are doing exactly what actual server-side 
implementation will do.

* Promote acceptance test driven development method and Microservices architectural style.

* Provide a way to publish changes in contracts that are immediately visible on both sides of the communication.

* Generate boilerplate test code used on the server side.

# manual
* `pom.xml`
    ```
    <properties>
        <spring-cloud.version>Finchley.RELEASE</spring-cloud.version>
        <spring-cloud-contract.version>2.0.0.RELEASE</spring-cloud-contract.version>
    </properties>
    ```
    ```
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-contract-verifier</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-contract-wiremock</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>spring-mock-mvc</artifactId>
        <version>3.0.0</version>
        <scope>test</scope>
    </dependency>
    ```
    ```
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>    
    ```
## Server / Producer side
* Add the base class
    * **Groovy**  
        `groovy/service/BaseClass.groovy`
        ```
        class BaseClass extends Specification {
            void setup() {
                RestAssuredMockMvc.standaloneSetup(new PersonRestController())
        
                Mock(PersonService) {
                    findPersonById(1) >> new Person(1, "foo", "bee")
                }
            }
        }
        ```
        configure plugins:
        ```
        <plugin>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-contract-maven-plugin</artifactId>
            <version>${spring-cloud-contract.version}</version>
            <extensions>true</extensions>
            <configuration>
                <baseClassForTests>service.BaseClass</baseClassForTests>
                <testFramework>SPOCK</testFramework>
            </configuration>
        </plugin>
        
        <plugin>
            <groupId>org.codehaus.gmavenplus</groupId>
            <artifactId>gmavenplus-plugin</artifactId>
            <version>1.6.1</version>
            <executions>
                <execution>
                    <goals>
                        <goal>compileTests</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <testSources>
                    <testSource>
                        <directory>${project.basedir}/src/test/groovy</directory>
                        <includes>
                            <include>**/*.groovy</include>
                        </includes>
                    </testSource>
                    <testSource>
                        <directory>${project.build.directory}/generated-test-sources/contracts</directory>
                        <includes>
                            <include>**/*.groovy</include>
                        </includes>
                    </testSource>
                </testSources>
            </configuration>
        </plugin>
        
        <plugin>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>2.22.0</version>
            <configuration>
                <includes>
                    <include>**/*Spec.java</include>
                </includes>
            </configuration>
        </plugin>
        ```
    
    * **JUnit**
        ```
        @RunWith(SpringRunner.class)
        @SpringBootTest(classes = ServiceApplication.class)
        public abstract class BaseClass {
        
            @Autowired
            PersonRestController personRestController;
        
            @MockBean
            PersonService personService;
        
            @Before
            public void setup() {
                RestAssuredMockMvc.standaloneSetup(personRestController);
        
                Mockito.when(personService.findPersonById(1))
                        .thenReturn(new Person(1, "foo", "bee"));
            }
        
        }
        ```
        configure plugin:
        ```
        <plugin>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-contract-maven-plugin</artifactId>
            <version>${spring-cloud-contract.version}</version>
            <extensions>true</extensions>
            <configuration>
                <baseClassForTests>service.BaseClass</baseClassForTests>
            </configuration>
        </plugin>
        ```

## client
* `pom.xml`
    ```
    <dependency>
    	<groupId>org.springframework.cloud</groupId>
    	<artifactId>spring-cloud-starter-contract-stub-runner</artifactId>
    	<scope>test</scope>
    </dependency>
    ```
* `client/ClientApplicationTests`
    ```
    @RunWith(SpringRunner.class)
    @SpringBootTest
    @AutoConfigureStubRunner(ids = {"com.example:rest-contract-service:0.0.1-SNAPSHOT:stubs:8100"}, 
            stubsMode = StubRunnerProperties.StubsMode.LOCAL)
    public class ClientApplicationTests
    ```
    
    Note that we could also load the stub inside the class (without using `@AutoConfigureStubRunner`):
    ```
    @Rule
    public StubRunnerRule stubRunnerRule = new StubRunnerRule()
            .downloadStub("com.example", "rest-contract-service", "0.0.1-SNAPSHOT", "stubs")
            .withPort(8100)
            .stubsMode(StubRunnerProperties.StubsMode.LOCAL);
    ```
* Remember to put `com.example:rest-contract-service:0.0.1-SNAPSHOT-stubs` in your `.m2` local maven repository
under `com/example/rest-contract-service/0.0.1-SNAPSHOT`.