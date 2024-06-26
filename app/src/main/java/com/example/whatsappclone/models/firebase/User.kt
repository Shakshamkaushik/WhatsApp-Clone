package com.example.whatsappclone.models.firebase

class User {
    private var name: String? = null
    private var uid: String? = null
    private var profilePictureUrl: String? = null
    private var countryCode: String? = null
    private var mobileNumber: String? = null
    private var isLastSeenVisisble: Boolean = false
    private lateinit var lastSeen: String
    private lateinit var profileStatus: String
    private var isOnline: Boolean = false
    private var statusUpdatedOn: String? = null
    private lateinit var profileCreatedOn: String
    private var isReadReceiptEnabled: Boolean = true

    constructor()

    constructor(
        name: String?,
        uid: String?,
        profilePictureUrl: String?,
        countryCode: String?,
        mobileNumber: String?,
        isLastSeenVisible: Boolean,
        lastSeen: String,
        profileStatus: String,
        isOnline: Boolean,
        statusUpdatedOn: String?,
        profileCreatedOn: String,
        isReadReceiptEnabled: Boolean
    ) {
        this.name = name
        this.uid = uid
        this.profilePictureUrl = profilePictureUrl
        this.countryCode = countryCode
        this.mobileNumber = mobileNumber
        this.isLastSeenVisisble = isLastSeenVisible
        this.lastSeen = lastSeen
        this.profileStatus = profileStatus
        this.isOnline = isOnline
        this.statusUpdatedOn = statusUpdatedOn
        this.profileCreatedOn = profileCreatedOn
        this.isReadReceiptEnabled = isReadReceiptEnabled
    }

    constructor(user: User) {
        this.name = user.name
        this.uid = user.uid
        this.profilePictureUrl = user.profilePictureUrl
        this.countryCode = user.countryCode
        this.mobileNumber = user.mobileNumber
        this.isLastSeenVisisble = user.isLastSeenVisisble
        this.lastSeen = user.lastSeen
        this.profileStatus = user.profileStatus
        this.isOnline = user.isOnline
        this.statusUpdatedOn = user.statusUpdatedOn
        this.profileCreatedOn = user.profileCreatedOn
        this.isReadReceiptEnabled = user.isReadReceiptEnabled
    }


    fun getName() = this.name
    fun getUid() = this.uid
    fun getProfilePictureUrl() = this.profilePictureUrl
    fun getCountryCode() = this.countryCode
    fun getMobileNumber() = this.mobileNumber
    fun getIsLastSeenVisisble() = this.isLastSeenVisisble
    fun getLastSeen() = this.lastSeen
    fun getProfileStatus() = this.profileStatus
    fun getIsOnline() = this.isOnline
    fun getStatusUpdateOn() = this.statusUpdatedOn
    fun getProfileCReatedOn() = this.profileCreatedOn
    fun getIsReadReceiptOn() = this.isReadReceiptEnabled

    fun setName(name: String?) {
        this.name = name
    }

    fun setProfilePictureUrl(profilePictureUrl: String) {
        this.profilePictureUrl = profilePictureUrl
    }

    fun setCountryCode(countryCode: String?) {
        this.countryCode = countryCode
    }

    fun setMobileNumber(mobileNumber: String?) {
        this.mobileNumber = mobileNumber
    }

    fun setIsLastSeenVisible(isLastSeenVisible: Boolean) {
        this.isLastSeenVisisble = isLastSeenVisible
    }

    fun setLastSeen(lastSeen: String) {
        this.lastSeen = lastSeen
    }

    fun setProfileStatus(profileStatus: String) {
        this.profileStatus = profileStatus
    }

    fun setIsOnline(isOnline: Boolean) {
        this.isOnline = isOnline
    }

    fun setIsReadReceiptEnabled(isReadReceiptEnabled: Boolean) {
        this.isReadReceiptEnabled = isReadReceiptEnabled
    }

    override fun toString(): String {
        return "contact: +${this.countryCode} ${this.mobileNumber}\n"
    }
}