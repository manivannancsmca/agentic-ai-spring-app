package com.user.service.app.mcp;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.user.service.app.dto.UserDto;
import com.user.service.app.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserMcpTools {

    private final UserService userService;

    @Tool(name = "get_user_by_id", 
          description = "Retrieve a user by their unique ID. Returns user details including username, email, and active status.")
    public UserDto getUserById(
            @ToolParam(description = "The unique identifier of the user") Long id) {
        log.info("MCP Tool invoked: get_user_by_id({})", id);
        return userService.getUserById(id);
    }

    @Tool(name = "list_all_users",
          description = "List all registered users in the system.")
    public List<UserDto> listAllUsers() {
        log.info("MCP Tool invoked: list_all_users()");
        return userService.listUsers();
    }

    @Tool(name = "create_new_user",
          description = "Create a new user account with username, email, and full name.")
    public UserDto createNewUser(
            @ToolParam(description = "Unique username") String username,
            @ToolParam(description = "Valid email address") String email,
            @ToolParam(description = "Full legal name") String fullName) {
        log.info("MCP Tool invoked: create_new_user({}, {})", username, email);
        return userService.createUser(username, email, fullName);
    }
}
