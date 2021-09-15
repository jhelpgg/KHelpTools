package khelp.sound

import khelp.thread.observable.Observer

internal class SoundCacheElement(val soundSource : SoundSource,
                                 var sound : Sound? = null,
                                 var stateObserver : Observer<SoundState>? = null)
