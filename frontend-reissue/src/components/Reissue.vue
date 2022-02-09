<template>
  <div id="app">
    <div class="input_st">
      <a-input-search
        placeholder="设置全局cookie"
        enter-button="设置"
        @search="onClick"
      />
    </div>

    <a-table
      :loading="loading"
      :columns="columns"
      :data-source="data"
      :rowKey="(re) => re.idAnchorpointreissue"
      :pagination="pagination"
      @change="handleTableChange"
    >
      <p slot="expandedRowRender" slot-scope="record" style="margin: 0">
        三方入参:<br />
        {{ record.params }}

        <br />
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
          :placeholder="`Search ${column.dataIndex}`"
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
          Search
        </a-button>
        <a-button
          size="small"
          style="width: 90px"
          @click="
            () => handleReset(clearFilters, selectedKeys, column.dataIndex)
          "
        >
          Reset
        </a-button>
      </div>
      <a-icon
        slot="filterIcon"
        slot-scope="filtered"
        type="search"
        :style="{ color: filtered ? '#108ee9' : undefined }"
      />
      <template slot="customRender" slot-scope="text, record, index, column">
        <span v-if="searchText && searchedColumn === column.dataIndex">
          <template
            v-for="(fragment, i) in text
              .toString()
              .split(new RegExp(`(?<=${searchText})|(?=${searchText})`, 'i'))"
          >
            <mark
              v-if="fragment.toLowerCase() === searchText.toLowerCase()"
              :key="i"
              class="highlight"
              >{{ fragment }}</mark
            >
            <template v-else>{{ fragment }}</template>
          </template>
        </span>
        <template v-else>
          {{ text }}
        </template>
      </template>

      <template slot="cookie" slot-scope="text, record">
        <EditableCell
          :text="text"
          @change="(value) => onCellChange(record, value)"
        />
      </template>

      <template slot="operation" slot-scope="text, record">
        <a-button type="primary" @click="() => onReissue(record)">
          执行重放
        </a-button>
      </template>
    </a-table>

    <slot></slot>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator'
import EditableCell from '@/components/EditableCell.vue'
@Component({
  components: { EditableCell },
})
export default class Reissue extends Vue {
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
  loading = false
  btLoading = false
  columns = [
    {
      dataIndex: 'idAnchorpointreissue',
      title: '锚点重放id',
      rowKey: 'idAnchorpointreissue',
    },
    {
      title: 'bean名称',
      dataIndex: 'beanName',
      key: 'beanName',
      scopedSlots: {
        filterDropdown: 'filterDropdown',
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
        customRender: 'customRender',
      },
    },
    {
      title: '锚点编码',
      dataIndex: 'cd',
      key: 'cd',
      width: 100,
      scopedSlots: {
        filterDropdown: 'filterDropdown',
        filterIcon: 'filterIcon',
        customRender: 'customRender',
      },
      onFilter: (value, record) => {
        console.log(record)
        record.name.toString().toLowerCase().includes(value.toLowerCase())
      },
    },

    {
      title: '插入时间',
      key: 'insertTime',
      dataIndex: 'insertTime',
    },

    {
      title: 'cookie',
      key: 'tk',
      dataIndex: 'tk',

      scopedSlots: { customRender: 'cookie' },
    },

    {
      title: '三方入参',
      key: 'params',
      dataIndex: 'params',
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

  data = []

  handleTableChange(pagination, filters, sorter) {
    const pager = { ...this.pagination }
    pager.current = pagination.current
    this.pagination = pager

    this.fetch(this.pagination)
  }
  handleReset(clearFilters, selectedKeys, dataIndex) {
    this.pagination.anchorPointReissue[dataIndex] = ''
    clearFilters()
    this.searchText = ''
  }

  onCellChange(record, value) {
    record.tk = value
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
    this.btLoading = true
    try {
      var res = await this.axios.post('/logs/sendReissue/', column)
    } catch (error) {
      this.$message.success({
        content: '重放执行完成!',
        key: column.idAnchorpointreissue,
        duration: 2,
      })
    }

    if (!res || !res.data.success) {
      alert('重放失败：' + column.beanName + '.' + column.method)
    }
    this.$message.success({
      content: '重放执行完成,返回结果：' + res.data.result,
      key: column.idAnchorpointreissue,
      duration: 10,
    })
  }

  async handleSearch(selectedKeys, confirm, dataIndex) {
    this.loading = true
    this.pagination.anchorPointReissue[dataIndex] = selectedKeys[0]
    var res = await this.axios.post('/logs/reissue', {
      current: 1,
      size: '10',
      anchorPointReissue: this.pagination.anchorPointReissue,
    })

    this.pagination.total = res.data.pages

    res.data.records.forEach((e) => {
      if (e.tk == undefined || e.tk == null) {
        e.tk = this.tk
      }
    })

    this.data = res.data.records
    this.loading = false
  }

  async fetch(page) {
    this.loading = true
    var res = await this.axios.post('/logs/reissue', page)

    this.pagination.total = res.data.pages
    this.loading = false

    res.data.records.forEach((e) => {
      if (e.tk == undefined || e.tk == null) {
        e.tk = this.tk
      }
    })

    this.data = res.data.records
  }

  mounted() {
    this.fetch(this.pagination)
  }
}
</script>

<style lang="scss" scoped></style>
