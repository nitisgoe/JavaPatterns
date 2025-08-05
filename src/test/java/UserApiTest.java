import client.APIClient;
import client.APIResponse;
import exceptions.ApiException;
import models.request.CreateUserRequest;
import models.response.User;
import models.response.UserRole;
import org.testng.annotations.*;
import static org.testng.Assert.*;

public class UserApiTest {
    private APIClient apiClient;
    private final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @BeforeClass
    public void setUp() {
        apiClient = new APIClient(BASE_URL);
    }

    @Test(description = "Test creating a new user with type-safe request and response")
    public void testCreateUser() throws ApiException {
        // Create type-safe request
        CreateUserRequest request = new CreateUserRequest(
                "johndoe123",
                "john.doe@example.com",
                "John",
                "Doe",
                30,
                UserRole.USER
        );

        APIResponse<User> response = apiClient.post("/users", request, User.class);
        assertTrue(response.isSuccess(), "API call should succeed");
        assertEquals(response.getStatusCode(), 201, "Status code should be 201 for created resource");

        User createdUser = response.getData();
        assertNotNull(createdUser, "Response should contain user data");

        // Type-safe assertions - compiler checks these at compile time
        assertNotNull(createdUser.getId(), "User ID should not be null");
        assertEquals(createdUser.getUsername(), request.getUsername(), "Username should match request");
    }
}
