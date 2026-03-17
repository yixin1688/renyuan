<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  createEmployee,
  deleteEmployee,
  getEmployees,
  updateEmployee,
} from './api/employee'

const loading = ref(false)
const dialogVisible = ref(false)
const submitting = ref(false)
const isEdit = ref(false)
const currentId = ref(null)

const query = reactive({
  keyword: '',
  page: 1,
  size: 10,
})

const pageData = reactive({
  list: [],
  total: 0,
})

const formRef = ref()
const form = reactive({
  employeeNo: '',
  name: '',
  gender: '',
  phone: '',
  email: '',
  department: '',
  title: '',
  hireDate: '',
  status: 'ACTIVE',
})

const rules = {
  employeeNo: [{ required: true, message: '请输入工号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  email: [{ type: 'email', message: '请输入正确邮箱', trigger: 'blur' }],
}

const resetForm = () => {
  currentId.value = null
  form.employeeNo = ''
  form.name = ''
  form.gender = ''
  form.phone = ''
  form.email = ''
  form.department = ''
  form.title = ''
  form.hireDate = ''
  form.status = 'ACTIVE'
}

const loadData = async () => {
  loading.value = true
  try {
    const data = await getEmployees({
      keyword: query.keyword,
      page: query.page - 1,
      size: query.size,
    })
    pageData.list = data.content
    pageData.total = data.totalElements
  } catch (error) {
    ElMessage.error(error.message || '加载员工列表失败')
  } finally {
    loading.value = false
  }
}

const openCreateDialog = () => {
  isEdit.value = false
  dialogVisible.value = true
  resetForm()
}

const openEditDialog = (row) => {
  isEdit.value = true
  dialogVisible.value = true
  currentId.value = row.id
  form.employeeNo = row.employeeNo || ''
  form.name = row.name || ''
  form.gender = row.gender || ''
  form.phone = row.phone || ''
  form.email = row.email || ''
  form.department = row.department || ''
  form.title = row.title || ''
  form.hireDate = row.hireDate || ''
  form.status = row.status || 'ACTIVE'
}

const handleSearch = () => {
  query.page = 1
  loadData()
}

const handleReset = () => {
  query.keyword = ''
  query.page = 1
  loadData()
}

const handleSave = async () => {
  if (!formRef.value) {
    return
  }

  try {
    await formRef.value.validate()
  } catch (error) {
    return
  }

  submitting.value = true
  try {
    const payload = {
      employeeNo: form.employeeNo,
      name: form.name,
      gender: form.gender,
      phone: form.phone,
      email: form.email,
      department: form.department,
      title: form.title,
      hireDate: form.hireDate || null,
      status: form.status,
    }

    if (isEdit.value) {
      await updateEmployee(currentId.value, payload)
      ElMessage.success('员工信息已更新')
    } else {
      await createEmployee(payload)
      ElMessage.success('员工已新增')
    }

    dialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error(error.message || '保存失败')
  } finally {
    submitting.value = false
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确认删除员工“${row.name}”吗？`, '删除确认', {
      type: 'warning',
    })
    await deleteEmployee(row.id)
    ElMessage.success('员工已删除')
    loadData()
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

onMounted(() => {
  loadData()
})
</script>

<template>
  <div class="page">
    <el-card shadow="never" class="toolbar-card">
      <div class="toolbar-header">
        <div>
          <h2>员工档案管理</h2>
          <p>支持员工信息的查询、新增、编辑和删除。</p>
        </div>
        <el-button type="primary" @click="openCreateDialog">新增员工</el-button>
      </div>

      <el-form :inline="true" class="search-form">
        <el-form-item>
          <el-input
            v-model="query.keyword"
            clearable
            placeholder="按姓名或工号搜索"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table :data="pageData.list" border stripe v-loading="loading">
        <el-table-column prop="employeeNo" label="工号" min-width="120" />
        <el-table-column prop="name" label="姓名" min-width="100" />
        <el-table-column prop="gender" label="性别" min-width="80" />
        <el-table-column prop="department" label="部门" min-width="120" />
        <el-table-column prop="title" label="职位" min-width="140" />
        <el-table-column prop="phone" label="手机号" min-width="140" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="hireDate" label="入职日期" min-width="120" />
        <el-table-column prop="status" label="状态" min-width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'ACTIVE' ? 'success' : 'info'">
              {{ row.status === 'ACTIVE' ? '在职' : '离职' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="160">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEditDialog(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next"
          v-model:current-page="query.page"
          v-model:page-size="query.size"
          :page-sizes="[5, 10, 20, 50]"
          :total="pageData.total"
          @current-change="loadData"
          @size-change="() => { query.page = 1; loadData() }"
        />
      </div>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑员工' : '新增员工'"
      width="680px"
      @closed="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <div class="form-grid">
          <el-form-item label="工号" prop="employeeNo">
            <el-input v-model="form.employeeNo" placeholder="请输入工号" />
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input v-model="form.name" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="性别">
            <el-select v-model="form.gender" placeholder="请选择性别" clearable>
              <el-option label="男" value="男" />
              <el-option label="女" value="女" />
            </el-select>
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="form.phone" placeholder="请输入手机号" />
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item label="部门">
            <el-input v-model="form.department" placeholder="请输入部门" />
          </el-form-item>
          <el-form-item label="职位">
            <el-input v-model="form.title" placeholder="请输入职位" />
          </el-form-item>
          <el-form-item label="入职日期">
            <el-date-picker
              v-model="form.hireDate"
              type="date"
              placeholder="请选择入职日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-radio-group v-model="form.status">
              <el-radio value="ACTIVE">在职</el-radio>
              <el-radio value="INACTIVE">离职</el-radio>
            </el-radio-group>
          </el-form-item>
        </div>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
