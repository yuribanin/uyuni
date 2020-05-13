/**
 * Copyright (c) 2016-2020 SUSE LLC
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */
package com.redhat.rhn.domain.action.salt.build;

import com.redhat.rhn.common.hibernate.HibernateFactory;
import com.redhat.rhn.domain.action.salt.StateResult;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.ConstructorException;

import java.io.Serializable;
import java.util.*;

/**
 * ImageBuildActionResult
 */
public class ImageBuildActionResult implements Serializable {

    private Long serverId;
    private Long actionImageBuildId;
    private byte[] output;

    private ImageBuildActionDetails parentActionDetails;

    /**
     * @return the serverId
     */
    public Long getServerId() {
        return serverId;
    }

    /**
     * @param sid serverId to set
     */
    public void setServerId(Long sid) {
        this.serverId = sid;
    }

    /**
     * @return the actionApplyStatesId
     */
    public Long getActionImageBuildId() {
        return actionImageBuildId;
    }

    /**
     * @param actionId the actionApplyStatesId to set.
     */
    public void setActionImageBuildId(Long actionId) {
        this.actionImageBuildId = actionId;
    }

    /**
     * @return the parentActionDetails
     */
    public ImageBuildActionDetails getParentScriptActionDetails() {
        return parentActionDetails;
    }

    /**
     * @param parentActionDetailsIn the parentActionDetails to set
     */
    public void setParentScriptActionDetails(
            ImageBuildActionDetails parentActionDetailsIn) {
        this.parentActionDetails = parentActionDetailsIn;
    }

    /**
     * @return the output
     */
    public byte[] getOutput() {
        return output;
    }

    /**
     * @param outputIn the output
     */
    public void setOutput(byte[] outputIn) {
        this.output = outputIn;
    }

    /**
     * @return String version of the Script contents
     */
    public String getOutputContents() {
        return HibernateFactory.getByteArrayContents(getOutput());
    }

    /**
     * @return Optional with list of state results or empty
     */
    public Optional<List<StateResult>> getResult() {
        Yaml yaml = new Yaml();
        List<StateResult> result = new LinkedList<>();
        try {
            @SuppressWarnings("unchecked")
            Map<String, Map<String, Object>> payload = yaml.loadAs(getOutputContents(), Map.class);
            // column can be null, which in turn makes payload null
            if (payload == null) {
                return Optional.empty();
            }
            payload.entrySet().stream().forEach(e -> {
                result.add(new StateResult(e));
            });
        }
        catch (ConstructorException ce) {
            return Optional.empty();
        }
        return Optional.of(result);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ImageBuildActionResult)) {
            return false;
        }

        ImageBuildActionResult result = (ImageBuildActionResult) obj;

        return new EqualsBuilder()
                .append(this.getActionImageBuildId(), result.getActionImageBuildId())
                .append(this.getServerId(), result.getServerId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getActionImageBuildId())
                .append(getServerId())
                .toHashCode();
    }
}
