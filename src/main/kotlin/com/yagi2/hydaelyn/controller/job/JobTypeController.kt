package com.yagi2.hydaelyn.controller.job

import com.yagi2.hydaelyn.model.entity.JobType
import com.yagi2.hydaelyn.service.JobTypeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/jobtype")
class JobTypeController {

    @Autowired
    lateinit var service: JobTypeService

    @RequestMapping(value = "/all", method = arrayOf(RequestMethod.GET))
    fun selectAll(): List<JobType> = service.selectAll()

    @RequestMapping(value = "/search", method = arrayOf(RequestMethod.GET))
    fun search(@RequestParam(value = "name", required = false) name: String?,
               @RequestParam(value = "id", required = false) id: Int?): List<JobType> {

        val redundantList = mutableListOf<MutableList<JobType>>()

        name?.let {
            if (name.isNotBlank()) {
                redundantList.add(mutableListOf())
                redundantList[redundantList.size - 1].addAll(service.findByNameContain(name))
            }
        }

        id?.let {
            redundantList.add(mutableListOf())
            redundantList[redundantList.size - 1].addAll(service.findById(id))
        }

        val result = mutableListOf<JobType>()

        redundantList.forEach {
            it.forEach { jobType ->
                if (redundantList.all { it.contains(jobType) } && result.contains(jobType).not()) {
                    result.add(jobType)
                }
            }
        }

        return result
    }
}