import { Page, Touchscreen } from 'puppeteer'

// https://chromedevtools.github.io/devtools-protocol/tot/Input#method-synthesizePinchGesture
interface PinchParams {
  x: number
  y: number
  scaleFactor: number
  gestureSourceType: 'touch' | 'default' | 'mouse'
}

// https://chromedevtools.github.io/devtools-protocol/tot/Input#method-synthesizeScrollGesture
interface ScrollParams {
  x: number // X coordinate of the start of the gesture in CSS pixels.
  y: number // Y coordinate of the start of the gesture in CSS pixels.
  xDistance: number // positive to scroll left
  yDistance: number // positive to scroll up
  gestureSourceType: 'touch' | 'default' | 'mouse'
  speed?: number // Swipe speed in pixels per second
  xOverscroll?: number
  yOverscroll?: number
  preventFling?: boolean
  repeatCount?: number
  repeatDelayMs?: number
}

const PINCH_NAME = 'Input.synthesizePinchGesture'
const SCROLL_NAME = 'Input.synthesizeScrollGesture'

declare module 'puppeteer' {
  interface Touchscreen {
    _client: {
      send: (name: string, params: PinchParams | ScrollParams) => Promise<void>
    }
  }
  interface Page {
    dispatchPinch: (pinchParams: PinchParams) => Promise<void>
    dispatchScroll: (scrollParams: ScrollParams) => Promise<void>
    touchsceen: Touchscreen
  }
}

// puppeteer 1.17.0 has no api to implement touchmove
// since puppeteer is connected to chromium with chromeDevTools
// https://chromedevtools.github.io/devtools-protocol/tot/Input#method-dispatchTouchEvent
export default (page: Page) => {
  page.dispatchPinch = async pinchParams => {
    await page.touchscreen._client.send(PINCH_NAME, pinchParams)
  }
  page.dispatchScroll = async scrollParams => {
    await page.touchscreen._client.send(SCROLL_NAME, scrollParams)
  }
}
