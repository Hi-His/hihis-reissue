<template>
  <div class="editable-cell">
    <div v-if="editable" class="editable-cell-input-wrapper">
      <a-input
        :value="value"
        @change="handleChange"
        @pressEnter="check"
      /><a-icon type="check" class="editable-cell-icon-check" @click="check" />
    </div>
    <div v-else class="editable-cell-text-wrapper">
      {{ value || ' ' }}
      <a-icon type="edit" class="editable-cell-icon" @click="edit" />
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from 'vue-property-decorator'
@Component({})
export default class EditableCell extends Vue {
  @Prop()
  text

  editable = false
  value = ''

  handleChange(e) {
    const value = e.target.value
    this.value = value
  }
  check() {
    this.editable = false
    this.$emit('change', this.value)
  }
  edit() {
    this.editable = true
  }

  mounted() {
    this.value = this.text
  }
}
</script>

<style lang="scss" scoped></style>
