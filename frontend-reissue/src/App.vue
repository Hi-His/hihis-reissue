<template>
  <div id="app">
    <a-button v-if="!searchReissue" type="primary" @click="changeLogs"
      >查询失败日志</a-button
    >
    <a-button v-else type="primary" @click="changeLogs">查询成功日志</a-button>

    <div class="input_st">
      <a-input-search
        placeholder="设置全局cookie"
        enter-button="设置"
        @search="onClick"
      />
    </div>

    <Reissue v-if="searchReissue"> </Reissue>
    <a-table
      v-else
      :loading="loading"
      :columns="columns"
      :data-source="data"
      :rowKey="(re) => re.idAnchorpointlog"
      :pagination="pagination"
      @change="handleTableChange"
    >
      <p slot="expandedRowRender" slot-scope="record" style="margin: 0">
        调用三方入参:<br />
        {{ record.params }}

        <br />
        <br />
        三方返回结果: <br />
        {{ record.result }}
      </p>
      <div
        slot="filterDropdown"
        slot-scope="{
          setSelectedKeys,
          selectedKeys,
          confirm,
          clearFilters,
          column,
        }"
        style="padding: 8px"
      >
        <a-input
          v-ant-ref="(c) => (searchInput = c)"
          :placeholder="`搜索 ${column.title}`"
          :value="selectedKeys[0]"
          style="width: 188px; margin-bottom: 8px; display: block"
          @change="
            (e) => setSelectedKeys(e.target.value ? [e.target.value] : [])
          "
          @pressEnter="
            () => handleSearch(selectedKeys, confirm, column.dataIndex)
          "
        />
        <a-button
          type="primary"
          icon="search"
          size="small"
          style="width: 90px; margin-right: 8px"
          @click="() => handleSearch(selectedKeys, confirm, column.dataIndex)"
        >
          搜索
        </a-button>
        <a-button
          size="small"
          style="width: 90px"
          @click="
            () => handleReset(clearFilters, selectedKeys, column.dataIndex)
          "
        >
          重置
        </a-button>
      </div>
      <a-icon
        slot="filterIcon"
        slot-scope="filtered"
        type="search"
        :style="{ color: filtered ? '#108ee9' : undefined }"
      />

      <div slot="filterTime" slot-scope="{ confirm }" style="padding: 8px">
        <div style="padding-bottom: 10px">
          <a-range-picker
            :placeholder="['开始时间', '结束时间']"
            @change="onChange"
            @ok="onOk"
            show-time
          >
          </a-range-picker>
        </div>

        <a-button
          type="primary"
          icon="search"
          size="small"
          style="width: 90px; margin-right: 8px"
          @click="() => handleSearch([date], confirm, `timeSearch`)"
        >
          搜索
        </a-button>
      </div>

      <template slot="tkEditor" slot-scope="text, record">
        <EditableCell
          :text="text"
          @change="(value) => onCellChange(record, value, 'tk')"
        />
      </template>

      <template slot="methodEditor" slot-scope="text, record">
        <EditableCell
          :text="text"
          @change="(value) => onCellChange(record, value, 'method')"
        />
      </template>

      <template slot="beanEditor" slot-scope="text, record">
        <EditableCell
          :text="text"
          @change="(value) => onCellChange(record, value, 'beanName')"
        />
      </template>

      <template slot="operation" slot-scope="text, record">
        <a-button type="primary" @click="() => onReissue(record)">
          执行重放
        </a-button>
      </template>
    </a-table>

    <a-modal v-model="visible" title="返回结果" @ok="handleOk" width="1000px">
      <div>{{ res }}</div>
    </a-modal>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'

import Reissue from '@/components/Reissue.vue'
import EditableCell from '@/components/EditableCell.vue'
@Component({
  components: { Reissue, EditableCell },
})
export default class App extends Vue {
  searchReissue = false
  pagination = {
    current: 1,
    size: '10',
    total: 0,
    anchorPointReissue: {},
    anchorPointLog: {},
  }
  tk = 'tk=61b411962c73337cf6e9df3c'
  searchText = ''
  searchInput = null
  searchedColumn = ''

  res = ''
  visible = false

  handleOk() {
    this.visible = false
  }
  loading = false

  btLoading = false
  columns = [
    {
      title: 'bean名称',
      dataIndex: 'beanName',
      key: 'beanName',
      scopedSlots: {
        filterDropdown: 'filterDropdown',
        filterIcon: 'filterIcon',
        customRender: 'beanEditor',
      },
    },
    {
      title: '锚点编码',
      dataIndex: 'cd',
      key: 'cd',
      scopedSlots: {
        filterDropdown: 'filterDropdown',
        filterIcon: 'filterIcon',
        customRender: 'customRender',
      },
      onFilter: (value, record) => {
        console.log(record)
        record.name.toString().toLowerCase().includes(value.toLowerCase())
      },
      width: 120,
    },
    {
      title: '执行时长',
      key: 'execTime',
      dataIndex: 'execTime',
      width: 100,
    },
    {
      title: '插入时间',
      key: 'insertTime',
      dataIndex: 'insertTime',
      scopedSlots: {
        filterDropdown: 'filterTime',
        filterIcon: 'filterIcon',
        customRender: 'customRender',
      },
    },
    {
      title: '方法名称',
      key: 'method',
      dataIndex: 'method',
      scopedSlots: {
        filterDropdown: 'filterDropdown',
        filterIcon: 'filterIcon',
        customRender: 'methodEditor',
      },
    },
    {
      title: 'cookie',
      key: 'tk',
      dataIndex: 'tk',

      scopedSlots: { customRender: 'tkEditor' },
    },
    {
      title: '三方入参',
      key: 'params',
      dataIndex: 'params',
      ellipsis: true,
      scopedSlots: {
        filterDropdown: 'filterDropdown',
        filterIcon: 'filterIcon',
        customRender: 'customRender',
      },
    },
    {
      title: '三方返回结果',
      key: 'result',
      dataIndex: 'result',
      ellipsis: true,
      width: 350,
      scopedSlots: {
        filterDropdown: 'filterDropdown',
        filterIcon: 'filterIcon',
        customRender: 'customRender',
      },
    },
    {
      title: 'operation',
      dataIndex: 'operation',
      scopedSlots: { customRender: 'operation' },
    },
  ]

  date: any = []
  onChange(value, dateString) {
    this.date = value
  }
  onOk(value) {
    this.date = value
  }

  changeLogs() {
    this.searchReissue = !this.searchReissue
  }
  data = []

  handleTableChange(pagination, filters, sorter) {
    const pager = { ...this.pagination }
    pager.current = pagination.current
    this.pagination = pager

    this.fetch(this.pagination)
  }

  onCellChange(record, value, key) {
    record[key] = value
  }

  onClick(value) {
    let data = this.data
    this.data = []
    data.forEach((e) => {
      e.tk = value
    })

    this.$nextTick(() => {
      this.data = data
    })
    this.tk = value
  }

  async onReissue(column) {
    const key = Math.random() * 1000
    try {
      this.$message.loading({ content: 'Loading...', key })

      var res = await this.axios.post('/logs/sendPoint/', column)
    } catch (error) {
      this.$message.success({
        content: '重放执行完成!',
        key: column.idAnchorpointreissue,
        duration: 10,
      })
    } finally {
      this.$message.success({ content: '重放执行成功...', key, duration: 2 })
    }

    if (!res || !res.data.success) {
      alert('重放失败：' + column.beanName + '.' + column.method)
    }

    this.visible = true
    this.res = res.data.result
  }

  async handleSearch(selectedKeys, confirm, dataIndex) {
    this.loading = true
    this.pagination.anchorPointLog[dataIndex] = selectedKeys[0]
    var res = await this.axios.post('/logs', {
      current: 1,
      size: '10',
      anchorPointLog: this.pagination.anchorPointLog,
    })
    res.data.records.forEach((e) => {
      if (e.tk == undefined || e.tk == null) {
        e.tk = this.tk
      }
    })
    this.pagination.total = res.data.pages
    this.pagination.current = 1
    this.loading = false
    this.data = res.data.records
  }

  async fetch(page) {
    this.loading = true
    var res = await this.axios.post('/logs', page)

    res.data.records.forEach((e) => {
      if (e.tk == undefined || e.tk == null) {
        e.tk = this.tk
      }
    })

    this.pagination.total = res.data.pages
    this.loading = false
    this.data = res.data.records
  }

  handleReset(clearFilters, selectedKeys, dataIndex) {
    this.pagination.anchorPointLog[dataIndex] = ''
    clearFilters()
    this.searchText = ''
  }
  mounted() {
    this.fetch(this.pagination)
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
.input_st {
  width: 400px;
  padding: 20px;
  position: absolute;
  top: 0;
  right: 0;
}
.app {
  font-family: sans-serif;
  padding: 0;
  display: flex;
  padding: 16px 8px;
}

.app-content {
  flex: 1;
  height: 300px;
  margin-left: 8px;
  margin-right: 8px;
  box-shadow: 0 0 10px 1px #e9e9e9;
}
</style>
