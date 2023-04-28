package khelp.musicPlayer.player

import khelp.thread.TaskContext
import khelp.thread.flow.FlowData
import khelp.thread.observable.ObservableData
import khelp.thread.parallel
import khelp.utilities.collections.ThrowSet
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

object MediaSeeker
{
    private val seekingLaunched = AtomicBoolean(false)
    private val directoriesToExplore = ThrowSet<File>()
    private val musicFilesFlowData = FlowData<File>()
    private val imageFilesFlowData = FlowData<File>()
    private val seekingObservableData = ObservableData<Boolean>(false)

    val musicFilesFlow = this.musicFilesFlowData.flow
    val imagesFilesFlow = this.imageFilesFlowData.flow
    val seekingObservable = this.seekingObservableData.observable

    fun startSeek()
    {
        if (this.seekingLaunched.compareAndSet(false, true))
        {
            this.seekingObservableData.value(true)
            parallel(TaskContext.IO, this::seek)
        }
    }

    private fun seek()
    {
        var files = File.listRoots() ?: return

        for (file in files)
        {
            if (! file.exists() || ! file.canRead())
            {
                continue
            }

            this.directoriesToExplore.throwIn(file)
        }

        while (this.directoriesToExplore.notEmpty)
        {
            val directory = this.directoriesToExplore()

            files = directory.listFiles(MediaFilter) ?: continue

            for (file in files)
            {
                when
                {
                    file.isDirectory             -> this.directoriesToExplore.throwIn(file)
                    SoundFileFilter.accept(file) -> this.musicFilesFlowData.publish(file)
                    ImageFileFilter.accept(file) -> this.imageFilesFlowData.publish(file)
                }
            }
        }

        this.seekingLaunched.set(false)
        this.seekingObservableData.value(false)
    }
}
