import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../contexts/AuthContext';
import { useModal } from '../contexts/ModalContext';
import { useToast } from './Toast';

const EditProfileModal = () => {
  const { t } = useTranslation();
  const { user, updateUser } = useAuth();
  const { closeModal } = useModal();
  const { showToast } = useToast();
  const [formData, setFormData] = useState({
    firstName: user.firstName,
    lastName: user.lastName,
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevData => ({ ...prevData, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    updateUser(formData);
    showToast(t('profile_updated_success'), 'success');
    closeModal();
  };

  return (
    <div style={{ padding: '2.5rem' }}>
      <h3 style={{ marginBottom: '1rem' }}>{t('edit_profile')}</h3>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="firstName" className="form-label">{t('first_name')}</label>
          <input
            type="text"
            id="firstName"
            name="firstName"
            className="form-input"
            value={formData.firstName}
            onChange={handleInputChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="lastName" className="form-label">{t('last_name')}</label>
          <input
            type="text"
            id="lastName"
            name="lastName"
            className="form-input"
            value={formData.lastName}
            onChange={handleInputChange}
            required
          />
        </div>
        <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '1rem', marginTop: '1.5rem' }}>
          <button type="button" className="btn btn-secondary" onClick={closeModal}>{t('cancel')}</button>
          <button type="submit" className="btn btn-primary">{t('save_changes')}</button>
        </div>
      </form>
    </div>
  );
};

export default EditProfileModal;