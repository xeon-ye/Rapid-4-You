<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <el-form
        ref="postForm"
        :model="postForm"
        :rules="rules"
        class="form-container"
      >
        <!-- 头部固定工具栏 -->
        <sticky :z-index="1501" :class-name="'sub-navbar'">
          <el-button
            v-loading="operating"
            icon="el-icon-share"
            style="margin-left: 10px"
            type="success"
            @click="publish"
          >
            发布
          </el-button>
          <el-button icon="el-icon-refresh" @click="reset"> 重置 </el-button>
        </sticky>
        <!-- 表单内容区域 -->
        <div class="post-main-container">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item prop="title">
                <MDinput v-model="postForm.title" :maxlength="100" required>
                  标题
                </MDinput>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="博客类型:" prop="blogType">
                <el-select
                  v-model="postForm.blogType"
                  clearable
                  placeholder="请选择"
                  class="wd-100"
                >
                  <el-option
                    v-for="item in blogTypeOptions"
                    :key="item.key"
                    :label="item.display_name"
                    :value="item.key"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="是否置顶:" prop="isSticky">
                <el-select
                  v-model="postForm.isSticky"
                  clearable
                  placeholder="请选择"
                  class="wd-100"
                >
                  <el-option
                    v-for="item in isStickyOptions"
                    :key="item.key"
                    :label="item.display_name"
                    :value="item.key"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="博客分类:" prop="category">
                <el-select
                  v-model="postForm.category"
                  value-key="id"
                  clearable
                  placeholder="请选择"
                  class="wd-100"
                >
                  <el-option
                    v-for="item in categoryOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="博客归档:" prop="archive">
                <el-select
                  v-model="postForm.archive"
                  value-key="id"
                  clearable
                  placeholder="请选择"
                  class="wd-100"
                >
                  <el-option
                    v-for="item in archiveOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="图片路径:" prop="image">
                <el-input
                  v-model="postForm.image"
                  clearable
                  placeholder="请输入图片路径..."
                />
              </el-form-item>
            </el-col>
            <el-col :span="18">
              <el-form-item label="博客标签:" prop="tags">
                <el-select
                  v-model="postForm.tags"
                  multiple
                  value-key="id"
                  filterable
                  default-first-optiony
                  clearable
                  placeholder="请选择文章标签"
                  class="wd-100"
                >
                  <el-option
                    v-for="item in tagOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col>
              <el-form-item label="摘要:" prop="summary">
                <el-input
                  v-model="postForm.summary"
                  :rows="5"
                  type="textarea"
                  placeholder="请输入摘要..."
                  maxlength="600"
                  show-word-limit
                />
              </el-form-item>
              <el-form-item label="文章内容:" prop="content">
                <div style="height: 650px; width: 100%; display: inline-block">
                  <mavon-editor
                    v-model="postForm.content"
                    style="height: 100%; width: 100%"
                  />
                </div>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import Sticky from '@/components/Sticky' // 粘性header组件
import MDinput from '@/components/MDinput'
import crudArticle from '@/api/blog/article'
import crudCategory from '@/api/blog/category'
import crudArchive from '@/api/blog/archive'
import crudTag from '@/api/blog/tag'

export default {
  name: 'ArticleDetail',
  components: { Sticky, MDinput },
  props: {
    operation: {
      type: String,
      default: 'view'
    }
  },
  data() {
    return {
      blogTypeOptions: [
        { key: 'NORMAL', display_name: '普通' },
        { key: 'SUBJECT', display_name: '专题' },
        { key: 'ABOUT', display_name: '关于' }
      ],
      isStickyOptions: [
        { key: true, display_name: '是' },
        { key: false, display_name: '否' }
      ],
      categoryOptions: [],
      archiveOptions: [],
      tagOptions: [],
      postForm: {},
      rules: {
        title: [
          { required: true, message: '标题内容不能为空', trigger: 'blur' }
        ],
        blogType: [
          { required: true, message: '博客类型不能为空', trigger: 'change' }
        ],
        isSticky: [
          { required: true, message: '是否置顶不能为空', trigger: 'change' }
        ],
        category: [
          { required: true, message: '博客分类不能为空', trigger: 'change' }
        ],
        archive: [
          { required: true, message: '博客归档不能为空', trigger: 'change' }
        ],
        createTime: [
          {
            type: 'date',
            required: true,
            message: '发布时间不能为空',
            trigger: 'change'
          }
        ],
        image: [
          { required: true, message: '图片路径不能为空', trigger: 'blur' }
        ],
        summary: [
          { required: true, message: '文章摘要不能为空', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '文章内容不能为空', trigger: 'blur' }
        ]
      },
      operating: false
    }
  },
  created() {
    // 获取分类下拉
    const p1 = crudCategory.getAll().then((res) => {
      this.categoryOptions = res
    })
    // 获取归档下拉
    const p2 = crudArchive.getAll().then((res) => {
      this.archiveOptions = res
    })
    // 获取标签下拉
    const p3 = crudTag.getAll().then((res) => {
      this.tagOptions = res
    })

    // 菜单拉取成功后，展示页面
    Promise.all([p1, p2, p3]).then((data) => {
      // 如果不是新建，则根据id查询
      if (this.operation !== 'add') {
        const id = this.$route.params && this.$route.params.id
        this.fetchData(id)
      }
    })
  },
  methods: {
    fetchData(id) {
      crudArticle.get(id).then((res) => {
        this.postForm = res
      })
    },
    publish() {
      this.$refs['postForm'].validate((valid) => {
        if (valid) {
          // 调用修改接口
          this.operating = true
          if (this.operation === 'add') {
            crudArticle
              .create(this.postForm)
              .then((response) => {
                this.operating = false
                this.$notify({
                  title: '新建文章成功',
                  type: 'success'
                })
                this._goBackToList()
              })
              .catch(() => (this.operating = false))
          } else if (this.operation === 'edit') {
            crudArticle
              .update(this.postForm)
              .then((response) => {
                this.operating = false
                this.$notify({
                  title: '更新文章成功',
                  type: 'success'
                })
                this._goBackToList()
              })
              .catch(() => (this.loading = false))
          } else {
            throw new Error('未知操作类型')
          }
        }
      })
    },
    reset() {
      this.$refs.postForm.resetFields()
      this.$notify({
        title: '信息已重置',
        type: 'success'
      })
    },
    _goBackToList() {
      // 调用全局挂载的方法
      this.$store.dispatch('tagsView/delView', this.$route)
      // 返回上一步路由
      this.$router.go(-1)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
</style>
