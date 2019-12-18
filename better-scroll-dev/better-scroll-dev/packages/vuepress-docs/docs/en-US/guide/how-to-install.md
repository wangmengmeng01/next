# Install

## NPM

BetterScroll is hosted on NPM and executed with the following command:

```bash
npm install @better-scroll/core@next --save

// or

yarn add @better-scroll/core@next
```

The next step is to use it in the code. [webpack](https://webpack.js.org/) and other build tools support the introduction of code from `node_modules` :

``` js
import BScroll from '@better-scroll/core'
```

If it is the syntax of commonjs, as follows:

``` js
var BScroll = require('@better-scroll/scroll')
```

## script

BetterScroll also supports direct loading with script, which loads a BScroll object on the window after loading.
