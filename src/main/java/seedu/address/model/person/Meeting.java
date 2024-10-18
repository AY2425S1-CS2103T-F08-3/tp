package seedu.address.model.person;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Person's meetings in the Meetings field.
 * Guarantees: immutable
 */
public class Meeting {
    public static final String MESSAGE_CONSTRAINTS_LOCATION = "Location can take any values, "
            + "and it should not be blank";

    public static final String MESSAGE_CONSTRAINTS_TIME = "Start time must be before end time";

    /*
     * The first character of the location must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[^\\s].*";

    public final String location;
    public final LocalDateTime startTime;
    public final LocalDateTime endTime;
    public final Name personToMeet;

    /**
     * Constructs an {@code Meeting}.
     *
     * @param person A valid name of a person to meet.
     * @param startTime A valid starting time of the meeting.
     * @param endTime A valid ending time of the meeting.
     * @param location A valid location.
     */
    public Meeting(Name person, LocalDateTime startTime, LocalDateTime endTime, String location) throws CommandException {
        requireNonNull(person);
        requireNonNull(location);
        requireNonNull(startTime);
        requireNonNull(endTime);

        if (!isValidStartAndEndTime(startTime, endTime)) {
            throw new CommandException(MESSAGE_CONSTRAINTS_TIME);
        }

        this.startTime = startTime;
        this.endTime = endTime;

        if (!isValidLocation(location)) {
            throw new CommandException(MESSAGE_CONSTRAINTS_LOCATION);
        }

        this.location = location;

        this.personToMeet = person;
    }

    public static boolean isValidStartAndEndTime(LocalDateTime start, LocalDateTime end) {
        return start.isBefore(end);
    }

    /**
     * Returns true if a given string is a valid location.
     */
    public static boolean isValidLocation(String location) {
        return location.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if a given meeting overlaps with another meeting.
     */
    public boolean isOverlap(Meeting other) {
        boolean isStartBeforeOtherEnd = this.startTime.isBefore(other.endTime);
        boolean isEndAfterOtherStart = this.endTime.isAfter(other.startTime);

        // If both conditions are true, meetings overlap
        return isStartBeforeOtherEnd && isEndAfterOtherStart;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a, d MMMM yyyy");
        return "from " + formatter.format(startTime) + " to " + formatter.format(endTime) + " at " + location;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Meeting)) {
            return false;
        }

        Meeting otherMeeting = (Meeting) other;
        return personToMeet.equals(otherMeeting.personToMeet) && location.equals(otherMeeting.location)
                && startTime.equals(otherMeeting.startTime) && endTime.equals(otherMeeting.endTime);
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Name getPersonToMeet() {
        return personToMeet;
    }
}
