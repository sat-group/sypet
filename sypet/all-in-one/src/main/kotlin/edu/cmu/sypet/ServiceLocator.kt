package edu.cmu.sypet

interface ServiceLocator {
//    fun <T : Compiler> addCompiler()
//    fun <T : Learner> addLearner()
//    fun <T : Library> addLibrary()
//    fun <T : Oracle> addOracle()
//    fun <T : Output> addOutput()
//    fun <T : PetriNet> addPetriNet()
//    fun <T : Program> addProgram()
//    fun <T : Query> addQuery()
//    fun <T : Response> addResponse()
//    fun <T : Runner> addRunner()
//    fun <T : SatSolver> addSatSolver()
//    fun <T : SketchSolver> addSketchSolver()
//    fun <T : Sketcher> addSketcher()
//    fun <T : Spec> addSpecification()
//    fun <T : SyPetriNet> addSyPetriNet()
//    fun <T : Synthesiser> addSinthesiser()

    fun build(): ServiceLocator

    fun <T> get(): T
}
