package com.myblog;
// 为测试目标函数编写表驱动单元测试用例

import com.myblog.utility.UserRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class PermissionTest {
    private String userRole;
    private String requiredRole;
    private boolean expected;

    public PermissionTest(String userRole, String requiredRole, boolean expected) {
        this.userRole = userRole;
        this.requiredRole = requiredRole;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Admin", "User", true},
                {"User", "Admin", false},
                {"Admin", "Admin", true},
                {"User", "User", true},
                {"Guest", "Admin", false},
                {"Guest", "User", false},
                {"Admin", "Guest", true},
                {"User", "Guest", true},
                {"Guest", "Guest", true}
        });
    }

    @Test
    public void testHasPermission() {
        UserRole Permission = new UserRole();
        assertEquals(expected, Permission.hasPermission(userRole, requiredRole));
    }
}

