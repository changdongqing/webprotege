# P1 硬编码文案甄别清单

生成方式见《汉化详细设计》第 4.2 节。状态：✅ 已汉化 ｜ ⏭️ 跳过（理由见备注）。

## 1. 客户端 Java 硬编码（已汉化 45 处 / 跳过 10 处）

| 文件 | 原文 | 状态 | 备注 |
|---|---|---|---|
| match/SelectableCriteriaTypePresenter.java | Nothing to select | ✅ | 基类新增 Messages 重载构造器（GWT.create 兜底） |
| frame/AnnotationPropertyFrameEditor.java | Enter an entity name ×2 | ✅ | |
| editor/EditorPresenter.java | Nothing selected | ✅ | 复用既有 key `nothingSelected` |
| editor/EditorPresenter.java | No editor available for selection: … | ✅ | 新 key `editor.noEditorAvailable` |
| project/ProjectPresenter.java | Loading project. Please wait. | ✅ | 设计文档示例改造点 |
| lang/LangTagFilterPresenter.java | Language filter / No remaining languages | ✅ | |
| search/EntitySearchFilterTokenFieldPresenter.java | Add filter / No remaining filters | ✅ | |
| perspective/PerspectivePresenter.java | Nothing Here / Reset tab 确认文案 | ✅ | 拼接改为带占位符的单条消息 |
| perspective/PortletWidgetMapper.java | No view class specified | ✅ | |
| perspective/CreateFreshPerspectiveRequestHandlerImpl.java | Enter tab name | ✅ | |
| entity/CsvImportPresenter.java | Error uploading CSV file / Upload CSV | ✅ | |
| csv/CSVImportDialogController.java | Import CSV File / CSV import succeeded / N rows / Please wait. | ✅ | AutoFactory：新参数会进入 create() 签名，调用方已同步 |
| csv/CSVColumnRelationEditorViewImpl.java | Select value type | ✅ | |
| merge_add/SelectOptionDialogController.java | Select Merge Type | ✅ | |
| merge_add/SelectExistingOntologyDialogController.java | Select Ontology | ✅ | |
| merge_add/SelectOntologiesForMergeDialogController.java | Select Ontologies | ✅ | |
| merge_add/SelectOptionViewImpl(.ui.xml/.java) | 合并方式选择界面 3 条 | ✅ | UiBinder 字面文本移入 Messages |
| merge_add/NewOntologyMergeProjectWorkflow.java | 合并进度 2 条 + 成功提示 | ✅ | |
| merge_add/ExistingOntologyMergeProjectWorkflow.java | 合并进度 2 条 + 成功提示 | ✅ | |
| merge_add/UploadAndMergeAdditionsProjectsWorkflow.java | Upload ontologies | ✅ | |
| merge/MergeUploadedProjectWorkflow.java | 合并进度 4 条 + 成功提示 | ✅ | |
| merge/UploadAndMergeProjectWorkflow.java | Upload ontologies | ✅ | |
| chgpwd/ChangePasswordPresenter.java | guest 密码提示 / Please wait. | ✅ | |
| chgpwd/ChangePasswordViewImpl(.ui.xml/.java) | Current/New/Confirm password | ✅ | |
| bulkop/EditAnnotationsViewImpl(.ui.xml/.java) | 批量编辑界面 16 条 | ✅ | |
| sharing/SharingSettingsPresenter.java | Please wait | ✅ | |
| watches/WatchedEntitiesPortletPresenter.java | 无权限关注提示 | ✅ | |
| change/EntityChangesPortletPresenter.java | 无权限查看变更提示 | ✅ | |
| mail/ChangeEmailAddressPresenter.java | 邮箱修改相关 4 条 | ✅ | |
| signup/SignUpPresenter.java | 注册结果 5 条 | ✅ | |
| login/LoginPresenter.java | Please wait. | ✅ | |
| project/CreateNewProjectPresenter.java | 创建项目相关 6 条 | ✅ | |
| project/CreateNewProjectViewImpl.java | 项目名缺失弹窗 | ✅ | |
| project/ProjectPrefixDeclarationsPresenter.java | 前缀相关 4 条 | ✅ | |
| form/FormEditorPresenter.java | Please provide a label… | ✅ | |
| frame/ManchesterSyntaxFrameEditorPresenter.java | Enter commit message | ✅ | |
| frame/PropertyValueDescriptorEditorPresenter.java | 占位提示 5 条 | ✅ | |
| viz/LargeGraphMessageViewImpl.java | 大图提示（拼接→占位符） | ✅ | |
| viz/NoEdgeCriteriaPresenterFactory.java | No Edge | ✅ | |
| obo/OBORelationshipEditorImpl.java | 占位提示 2 条 | ✅ | |
| upload/FileUploadResponse.java | Invalid response | ✅ | |
| individualslist/IndividualsListPresenter.java | 批量删除确认（拼接→占位符） | ✅ | |
| hierarchy/DeleteEntitiesPresenter.java + IndividualsListPresenter | 实体类型词 class/individual 等 | ✅ | 新增 `entityType.*` 7 个 key 做类型名映射 |
| dispatch/DispatchServiceCallback.java | 错误弹窗标题 Error | ✅ | |
| app/ApplicationSettingsPresenter.java | Please wait | ✅ | |

### 跳过项（面向开发者/非界面文案）

| 文件 | 原文 | 理由 |
|---|---|---|
| obo/OBOTermSynonymEditor.java | Enter synonym name（已注释代码） | 死代码 |
| frame/ManchesterSyntaxFrameEditorPresenter.java | GWT.log 两条 | 日志 |
| sharing/SharingSettingEditorImpl.java | RuntimeException: Undefined sharing permission | 开发者异常 |
| library/text/ExpandingTextBoxImpl.java | RuntimeException: Cannot find pre element | 开发者异常 |
| match/RootCriteriaPresenter.java | RuntimeException ×2 | 开发者异常 |
| shortform/DictionaryLanguageSelectorPresenter.java | RuntimeException: No suitable factory… | 开发者异常 |
| dispatch/DispatchServiceCallback.java | GWT.log("Error", …) | 日志 |
| collection/CollectionViewImpl.java | Amino Acids / Add | 遗留演示路径（氨基酸示例集合），列入允许清单 |
| viz/LargeGraphMessageViewImpl 等 | GWT.log 若干 | 日志 |

## 2. 服务端高频文案（P2，已汉化 12 处）

| 文件 | 原文 | key |
|---|---|---|
| dispatch/validators/UserIsSignedInValidator.java | You must be signed in… | server.mustBeSignedIn |
| dispatch/validators/ApplicationPermissionValidator.java | You do not have permission for … | server.noPermissionForAction |
| dispatch/validators/ProjectPermissionValidator.java | Permission denied for … | server.permissionDeniedForActionId |
| project/chg/ChangeManager.java ×5 | You do not have permission to edit/create… | server.noPermission* |
| project/CreateNewProjectActionHandler.java ×2 | …create/upload projects | server.noPermission* |
| issues/DeleteEntityCommentHandler.java | …delete this comment（顺带修正原文 typo） | server.noPermissionDeleteComment |

locale 传播：`DispatchServlet` 每个请求把 `Accept-Language` 首选语言写入 `ServerMessages` ThreadLocal，请求结束清除；`webprotege-server-core/src/main/resources/messages_zh.properties` 为文案目录。

## 3. 邮件（P3，已汉化）

- 密码重置邮件：新增 `templates/password-reset-email-template.zh.html`；`mail.locale=zh`（置于部署的 mail.properties）时使用中文模板与主题。
- 评论通知邮件：新增 `templates/comment-notification-email-template.zh.html`；主题按 `mail.locale` 汉化。
- 关注通知邮件（watch-notification）：保持英文，后续按同模式扩展。
