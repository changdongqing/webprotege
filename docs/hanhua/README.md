# WebProtégé 简体中文语言包

本目录是 WebProtégé 界面汉化的工作区。设计方案见 [《汉化详细设计》](汉化详细设计.md)。

## 已实现能力

| 层级 | 范围 | 实现 |
|---|---|---|
| P0 | 客户端框架文案 329 条（Messages 287 + FormsMessages 42） | `Messages_zh.properties` / `FormsMessages_zh.properties` |
| P0 | locale 注册 | `webprotege-client/src/main/module.gwt.xml` 注册 `zh` / `zh_CN` |
| P1 | 客户端硬编码改造 45 处（含 3 个 UiBinder 视图、实体类型词） | 见 [hardcode-inventory.md](hardcode-inventory.md) |
| P2 | 服务端高频文案 12 处（未登录/权限/删除评论） | `ServerMessages` + `messages_zh.properties`（server-core） |
| P3 | 密码重置/评论通知邮件模板与主题 | `templates/*.zh.html` + `mail.locale` 配置 |
| P3 | 服务端错误页 | `forbidden.jsp` / `configuration-error.jsp` 按 Accept-Language 双语 |
| — | 中文字体栈 | `WebProtege.css` 追加 PingFang/Microsoft YaHei/Noto Sans CJK |

## 如何启用中文

1. **自动**：浏览器语言为中文（`Accept-Language: zh-CN` / `zh`）时自动进入中文界面，无需任何配置（宿主页 `WebProtege.jsp` 按 Accept-Language 注入 GWT locale）。
2. **强制**：URL 加 `?locale=zh_CN` 强制中文；`?locale=en` 强制英文。
3. **整站默认中文（私有化部署）**：将 `WebProtege.jsp` 中的
   `<meta name="gwt:property" content="locale=<%= request.getLocale() %>">`
   改为 `<meta name="gwt:property" content="locale=zh_CN">`。

## 系统邮件启用中文

在部署的 `mail.properties`（WebProtégé web-app 根目录，与 SMTP 配置同文件）增加一行：

```properties
mail.locale=zh
```

即启用密码重置/评论通知邮件的中文模板与主题。管理员也可以把 `templates/*.zh.html` 复制到数据目录覆盖原模板自行微调。

## 构建与校验

```bash
# 词条 parity 校验（key 覆盖、占位符、编码）——应输出 CHECK OK
python3 docs/hanhua/check_zh.py \
    webprotege-client/src/main/java/edu/stanford/bmir/protege/web/client/Messages.java \
    webprotege-client/src/main/resources/edu/stanford/bmir/protege/web/client/Messages_zh.properties \
    webprotege-client/src/main/java/edu/stanford/bmir/protege/web/client/FormsMessages.java \
    webprotege-client/src/main/resources/edu/stanford/bmir/protege/web/client/FormsMessages_zh.properties

# 全量构建（GWT 编译位于 package 阶段）
mvn -s .mvn/settings.xml -DskipTests package

# 本地 Docker 镜像（必须 --network=host；需传 WEBPROTEGE_VERSION）
docker build --network=host --build-arg WEBPROTEGE_VERSION=5.0.0-SNAPSHOT -t webprotege-zh .
docker compose up -d   # override 已指向 webprotege-zh:latest
```

上游升级后：运行 `docs/hanhua/extract_messages.py` 重新提取 → `check_zh.py` 报告新增/缺失 key → 补译 → 校验。

### 验证汉化是否真正生效（重要）

GWT 编译产物中，非 ASCII 词条会被混淆器按**单字拆分转义**输出（如“关于”呈现为 `\u5173\u4e8e`），因此直接搜完整中文词条会误判为“未翻译”。正确做法是解码 `\uXXXX` 转义后再校验：

```bash
python3 - <<'EOF'
import zipfile, re
war = zipfile.ZipFile('webprotege-client/target/webprotege-client-5.0.0-SNAPSHOT.war')
nc = war.read('webprotege/webprotege.nocache.js').decode('utf-8', errors='replace')
loc = dict(re.findall(r"\$intern_\d+\s*=\s*'(de|pt|zh|zh_CN|en|sl|default)'\s*,\s*\$intern_\d+\s*=\s*'([A-F0-9]{32})'", nc))
for name in ('zh', 'zh_CN'):
    js = war.read(f"webprotege/{loc[name]}.cache.js").decode('utf-8', errors='replace')
    strs = {s.encode().decode('unicode_escape') for s in re.findall(r'(?:\\u[0-9a-fA-F]{4})+', js)}
    strs = {s for s in strs if any('\u4e00' <= c <= '\u9fff' for c in s)}
    print(name, 'CJK strings:', len(strs))
EOF
```

`zh` 与 `zh_CN` 两个 permutation 均应输出 400 余个 CJK 字符串；`zh_CN` 无需单独的 `Messages_zh_CN.properties`（GWT locale 继承自动复用 `Messages_zh.properties`）。

## 允许清单（保持英文的部分）

- 第三方 GWT 库内部文案：widgetmap（布局编辑器右键菜单）、graphtree、codemirror-gwt。
- 遗留演示路径：collection 视图的 "Amino Acids" 示例标题。
- `About` 弹窗的版本信息（VersionInfo.properties）。
- 日志与开发者异常信息。
- 数据级内容（实体标签、表单字段标签、评论正文等）不属于界面汉化——系统原生支持数据多语言：界面切到 zh_CN 后自动优先显示 `@zh` 标注的实体标签与表单标签（见 `LanguageMapCurrentLocaleMapper`）。
- `pagination.rows` 与标签删除确认两条复数消息通过 GWT 内联复数语法 `{n,plural,one#…|other#…}` 汉化，其余英文变体语句均已单条化。

## 已知限制

1. 实体删除确认框中的类型词（类/对象属性等）已汉化，但 `merge.description` 等消息中运行时拼接的 OWL API 类型词仍可能以英文出现。
2. 关注通知邮件（watch notification）暂保持英文。
3. 部分深层对话框（如可视化图谱设置）可能存在零星未覆盖文案，随上游同步持续补齐。
