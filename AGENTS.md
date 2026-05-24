# AGENTS.md

> 本文件为项目级 Agent 规则。放在根目录时定义全局约束；放在子目录时定义局部上下文。
> 如需更具体的模块规则，在子目录下新建 AGENTS.md 即可覆盖父目录规则。

## 项目信息
- **名称**: MyBlog
- **类型**: [遗留维护]
- **技术栈**: springboot+vue
- **创建日期**: 2025.12.25

## 关键目录
| 目录 | 职责 |
|------|------|
| MyBlog | 后端文件 |
| my-blog-frontend2 | 前端文件 |
| key | 与服务器连接ssh pem文件 |

## 验证命令
```powershell
# 环境检查
# 测试运行
# 构建命令
```

## 项目专属约束

## 服务器连接

| 属性 | 值 |
|------|-----|
| **服务器 IP** | 47.108.171.144 |
| **SSH 端口** | 22 |
| **登录用户** | root |
| **主机名** | iZ2vcj0j3huiivax738zoiZ |
| **密钥文件** | `key/myblogServerSSHKEY.pem` |

### 连接命令

```bash
# 标准连接
ssh -i key/myblogServerSSHKEY.pem root@47.108.171.144

# 首次连接（自动接受主机密钥，不保存到 known_hosts）
ssh -o StrictHostKeyChecking=accept-new -o UserKnownHostsFile=/dev/null -i key/myblogServerSSHKEY.pem root@47.108.171.144
```

### 注意事项
- 密钥文件在 Windows Git Bash 环境下 `chmod 600` 可能无法生效；如遇权限拒绝，可添加 `-o IdentitiesOnly=yes` 参数强制使用该密钥。
- 服务器 OpenSSH 配置未启用后量子密钥交换算法，连接时会出现安全警告，不影响正常使用。
- 该服务器为生产/部署环境，执行危险操作前请确认影响范围。

## 数据库与缓存连接信息

| 服务 | 属性 | 值 |
|------|------|-----|
| **MySQL** | 主机 | 47.108.171.144 |
| | 端口 | 3306 |
| | 数据库 | blog |
| | 用户名 | root |
| | 密码 | `bl.264979` |
| | root 远程访问 | 已允许 (`%`) |
| **Redis** | 主机 | 47.108.171.144 |
| | 端口 | 6379 |
| | 密码 | `jasperpasswordforredis264979` |
| | 版本 | 6.2.7（支持 ACL） |

## MCP 服务器配置

配置文件位于 `.vscode/mcp.json`。

### 已配置的 MCP 服务器

| 名称 | 包名 | 用途 | 只读 |
|------|------|------|------|
| **mysql-readonly** | `@hovecapital/read-only-mysql-mcp-server` | MySQL 只读查询、表结构查看 | ✅ 是（仅允许 SELECT） |
| **redis** | `@modelcontextprotocol/server-redis` | Redis 键值查询与操作 | ⚠️ 否（支持 set/delete） |

### MySQL MCP 连接参数
```json
{
  "command": "npx",
  "args": ["-y", "@hovecapital/read-only-mysql-mcp-server"],
  "env": {
    "DB_HOST": "47.108.171.144",
    "DB_PORT": "3306",
    "DB_DATABASE": "blog",
    "DB_USERNAME": "root",
    "DB_PASSWORD": "bl.264979"
  }
}
```

### Redis MCP 连接参数
```json
{
  "command": "npx",
  "args": [
    "-y",
    "@modelcontextprotocol/server-redis",
    "redis://:jasperpasswordforredis264979@47.108.171.144:6379"
  ]
}
```

### ⚠️ Redis 只读安全建议
官方 `@modelcontextprotocol/server-redis` MCP 服务器**并非应用层只读**，它提供了 `set` 和 `delete` 等写操作工具。如需严格限制为只读，建议在 Redis 服务器上创建 ACL 只读用户：

```bash
# 通过 SSH 登录服务器后执行
redis-cli -a 'jasperpasswordforredis264979' ACL SETUSER mcp_readonly on >mcp_readonly_pass ~* +@read
```

然后在 `.vscode/mcp.json` 中将 Redis URL 改为：
```
redis://mcp_readonly:mcp_readonly_pass@47.108.171.144:6379
```

### 🔐 安全提示
- `.vscode/mcp.json` 中包含数据库明文密码，**请勿将其提交到 Git 仓库**。
- 当前根目录 `.gitignore` 未排除 `.vscode/`，如需保护请添加：
  ```
  .vscode/mcp.json
  ```

## 网络代理配置

本项目所在环境的 Git 访问 GitHub 时可能需要代理支持。

### 代理地址
```bash
export https_proxy=http://127.0.0.1:7897
export http_proxy=http://127.0.0.1:7897
export all_proxy=socks5://127.0.0.1:7897
```

### 使用场景
- **需要使用代理**：`git push` / `git pull` / `git clone` 等操作连接 GitHub 失败时
- **不需要使用代理**：一般情况下无需设置，访问国内服务时建议关闭代理

### 快速命令
```bash
# 临时使用代理（当前终端会话有效）
export https_proxy=http://127.0.0.1:7897 http_proxy=http://127.0.0.1:7897 all_proxy=socks5://127.0.0.1:7897

# 验证代理是否生效
curl -I https://github.com

# 清除代理（当前终端会话）
unset https_proxy http_proxy all_proxy
```

## 后端部署方式

后端使用 **systemd** 管理，通过环境变量文件注入敏感配置。

### 服务文件
- **模板**: `MyBlog/myblog.service.template`
- **服务器路径**: `/etc/systemd/system/myblog.service`
- **环境变量文件**: `/usr/blog/myblog.env`（权限 `600`，仅 root 可读）

### 环境变量配置
`/usr/blog/myblog.env` 内容示例：
```bash
MYSQL_PASSWORD=bl.264979
REDIS_PASSWORD=jasperpasswordforredis264979
```

### 常用命令
```bash
# 查看状态
systemctl status myblog

# 启动/停止/重启
systemctl start myblog
systemctl stop myblog
systemctl restart myblog

# 查看日志
journalctl -u myblog -f
tail -f /usr/blog/logs/myblog.log
```

### 部署流程
1. 本地构建：`./mvnw.cmd clean package -DskipTests`
2. 上传 JAR：`scp MyBlog/target/MyBlog-0.0.1-SNAPSHOT.jar root@47.108.171.144:/usr/blog/target/`
3. 重启服务：`systemctl restart myblog`

### 注意事项
- 服务器上 `/usr/bin/java` 指向 Java 11，但 Spring Boot 3.x 需要 Java 17
- systemd service 中已硬编码 Java 17 路径：`/opt/amazon-corretto-17.0.11.9.1-linux-x64/bin/java`
- `application.yaml` 中密码使用 `${MYSQL_PASSWORD:bl.264979}` 占位符，开发环境使用默认值，生产环境由 `myblog.env` 覆盖

## 外部依赖
