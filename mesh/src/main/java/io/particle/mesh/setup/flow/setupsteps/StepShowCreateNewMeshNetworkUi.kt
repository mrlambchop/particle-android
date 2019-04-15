package io.particle.mesh.setup.flow.setupsteps

import io.particle.mesh.setup.flow.MeshSetupStep
import io.particle.mesh.setup.flow.Scopes
import io.particle.mesh.setup.flow.context.SetupContexts
import io.particle.mesh.setup.flow.modules.FlowUiDelegate


class StepShowCreateNewMeshNetworkUi(private val flowUi: FlowUiDelegate) : MeshSetupStep() {

    override suspend fun doRunStep(ctxs: SetupContexts, scopes: Scopes) {
        if (ctxs.mesh.newNetworkCreatedSuccessfully) {
            return
        }

        flowUi.showCreatingMeshNetworkUi()
    }

}