# mouse-wheel

This plugin supports for mouse wheel for core scrolling and some plugins in PC.

## Install

```bash
npm install @better-scroll/mouse-wheel@next --save

// or

yarn add @better-scroll/mouse-wheel@next
```

:::tip
Currently, the core scrolling has supported mouse wheel.
Plugins supported for mouse wheel action are **slide** and **wheel**.
:::

# Usage

- Add mouse wheel feature for core scrolling

  ```js
    import BScroll from '@better-scroll/core'
    import MouseWheel from '@better-scroll/mouse-wheel'
    BScroll.use(MouseWheel)

    new BScroll('.bs-wrap', {
      //...
      mouseWheel: {
        speed: 20,
        invert: false,
        easeTime: 300
      }
    })
  ```

- add mouse wheel feature for other plugins. In this case, you should use mouse-wheel plugin together with which plugin you will use.

  ```js
    import BScroll from '@better-scroll/core'
    import MouseWheel from '@better-scroll/mouse-wheel'
    import Slide from '@better-scroll/slide'

    BScroll.use(MouseWheel)
    BScroll.use(Slide)

    new BScroll('.bs-wrap', {
      scrollX: true,
      scrollY: false,
      slide: {
        loop: true,
        threshold: 100
      },
      momentum: false,
      bounce: false,
      stopPropagation: true,
      mouseWheel: {
        speed: 20,
        invert: false,
        easeTime: 300
      }
    })
  ```

## Demo

- core scroll

  <demo :hide-qrcode="true">
    <template slot="code-template">
      <<< @/examples/vue/components/core/mouse-wheel.vue?template
    </template>
    <template slot="code-script">
      <<< @/examples/vue/components/core/mouse-wheel.vue?script
    </template>
    <template slot="code-style">
      <<< @/examples/vue/components/core/mouse-wheel.vue?style
    </template>
    <core-mouse-wheel slot="demo"></core-mouse-wheel>
  </demo>

- wheel

- slide

  <demo :hide-qrcode="true">
    <template slot="code-template">
      <<< @/examples/vue/components/slide/pc.vue?template
    </template>
    <template slot="code-script">
      <<< @/examples/vue/components/slide/pc.vue?script
    </template>
    <template slot="code-style">
      <<< @/examples/vue/components/slide/pc.vue?style
    </template>
    <slide-pc slot="demo"></slide-pc>
  </demo>

## Options

### speed

The speed of mouse wheel.

- Type:number
- Default: 20

### invert

The `true` value means that the direction of mouse wheel and the direction of real scroll are opposite.

- Type: boolean
- Default: false

### easeTime

The ease time of the rolling animation. The unit is milliseconds.

- Type: number
- Default: 300
