# API Key 配置指南

## ⚠️ 当前问题

你配置的 API Key 格式无效，导致认证失败：
```
401 Unauthorized: Incorrect API key provided
```

## 📍 需要修改的位置

在 `src/main/resources/application.yml` 中，以下 **5 个位置** 需要使用相同的真实 API Key：

```yaml
# 位置 1: 第 47 行
langchain4j:
  open-ai:
    chat-model:
      api-key: sk-你的真实APIKey  # ← 替换这里

# 位置 2: 第 56 行
    streaming-chat-model:
      api-key: sk-你的真实APIKey  # ← 替换这里

# 位置 3: 第 65 行
    reasoning-chat-model:
      api-key: sk-你的真实APIKey  # ← 替换这里

# 位置 4: 第 72 行
routing-chat-model:
  api-key: sk-你的真实APIKey  # ← 替换这里

# 位置 5: 第 79 行 (已修复尖括号)
generate-app-name-model:
  api-key: sk-你的真实APIKey  # ← 替换这里
```

## 🔑 如何获取正确的 API Key

### 方法 1：通过阿里云控制台（推荐）

1. 访问：https://dashscope.console.aliyun.com/apiKey
2. 登录阿里云账号
3. 创建新 Key 或复制现有 Key
4. 确保格式类似：`sk-a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6`

### 方法 2：通过阿里云 API 网关

1. 访问：https://apigateway.console.aliyun.com/
2. 找到 DashScope 相关的应用
3. 查看 AppCode 或 API Key

## ✅ 验证 API Key 是否有效

可以使用 curl 命令测试：

```bash
curl https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions \
  -H "Authorization: Bearer sk-你的APIKey" \
  -H "Content-Type: application/json" \
  -d '{"model":"qwen-plus","messages":[{"role":"user","content":"hello"}]}'
```

如果返回正常的 JSON 响应（非 401 错误），说明 Key 有效。

## 🚫 常见错误

### ❌ 错误 1：包含尖括号
```yaml
api-key: <sk-xxxxx>  # 错误！
```

### ❌ 错误 2：包含空格或换行
```yaml
api-key: sk- xxxxx   # 错误！有空格
api-key: sk-
  xxxxx              # 错误！有换行
```

### ❌ 错误 3：使用了错误的 Key 类型
-DashScope API Key 格式：`sk-` + 32位字符串
- 不是 AccessKey ID / Secret（那是 OSS 用的）

## 💡 安全建议

1. **不要将真实 API Key 提交到 Git**
   - 确保 `.gitignore` 包含 `application.yml`
   - 或使用环境变量替代硬编码

2. **定期轮换 API Key**
   - 如果怀疑 Key 泄露，立即在控制台删除并重新生成

3. **使用环境变量（推荐用于生产）**
```yaml
api-key: ${DASHSCOPE_API_KEY}
```
然后设置环境变量：
```bash
export DASHSCOPE_API_KEY=sk-你的真实APIKey
```

## 📞 获取帮助

如果仍然遇到问题：
- 官方文档：https://help.aliyun.com/zh/model-studio/getting-started/first-api-call-to-qwen
- 错误码查询：https://help.aliyun.com/zh/model-studio/error-code
- 技术支持：提交工单至阿里云